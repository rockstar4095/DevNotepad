package com.example.devnotepad.ui.fragment_directions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.data.DirectionsRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DirectionsViewModel(application: Application) : AndroidViewModel(application) {
    private val directionsRepository: DirectionsRepository
    val allDirections: LiveData<List<DirectionOfStudy>>
    private val api: DevNotepadApi

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        api = retrofitInstance.create(DevNotepadApi::class.java)

        val directionDao = KnowledgeRoomDatabase.getDatabase(application).directionDao()
        directionsRepository = DirectionsRepository(directionDao)
        allDirections = directionsRepository.allDirections
    }

    private fun insertDirection(directionOfStudy: DirectionOfStudy) =
        viewModelScope.launch(Dispatchers.IO) {
            directionsRepository.insertDirection(directionOfStudy)
        }

    private fun deleteDirection(directionOfStudy: DirectionOfStudy) =
        viewModelScope.launch(Dispatchers.IO) {
            directionsRepository.deleteDirection(directionOfStudy)
        }

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    fun makeRequestForDirections() {
        api.getDirections().enqueue(object: Callback<List<DirectionOfStudy>> {
            override fun onResponse(call: Call<List<DirectionOfStudy>>, response: Response<List<DirectionOfStudy>>) {
                CoroutineScope(Dispatchers.IO).launch {
                    handleServerDirections(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<DirectionOfStudy>>, t: Throwable) {
                println("response unsuccessful: $t")
            }
        })
    }

    /**
     * Обрабатывает направления, полученные от сервера, с направлениями, хранящимися в локальной БД.
     * */
    private suspend fun handleServerDirections(directionsFromServer: List<DirectionOfStudy>) {

        // Проверка на отсутствие данных в таблице.
        if (isTableEmpty()) {
            // Вставка направлений в пустую таблицу.
            insertDirections(directionsFromServer)
            // Выход из метода.
            return
        }

        // Сравнение данных с сервера с локальными.
        matchDirectionsFromServerAndLocal(directionsFromServer)
    }

    /**
     * Проверяет таблицу с направлениями на наличие данных.
     * */
    private suspend fun isTableEmpty(): Boolean {
        return directionsRepository.getAllDirectionsSync().isEmpty()
    }

    /**
     * Вставляет список направлений в таблицу.
     * */
    private fun insertDirections(directions: List<DirectionOfStudy>) {
        for (direction in directions) {
            insertDirection(direction)
        }
    }

    /**
     * Сравнивает направления с сервера с локальными и вызывает методы вставки/обновления/удаления.
     * */
    private suspend fun matchDirectionsFromServerAndLocal(directionsFromServer: List<DirectionOfStudy>) {

        val serverDirectionsHashMap: HashMap<Int, DirectionOfStudy> = HashMap()
        val localDirectionsHashMap: HashMap<Int, DirectionOfStudy> = HashMap()

        for (direction in directionsFromServer) {
            serverDirectionsHashMap[direction.idFromServer] = direction
        }

        for (direction in directionsRepository.getAllDirectionsSync()) {
            localDirectionsHashMap[direction.idFromServer] = direction
        }

        insertNewDirections(serverDirectionsHashMap, localDirectionsHashMap)
        replaceRenewedDirections(serverDirectionsHashMap, localDirectionsHashMap)
        deleteAbsentDirections(serverDirectionsHashMap, localDirectionsHashMap)
    }

    /**
     * Вставляет новые направления в локальную БД.
     * */
    private fun insertNewDirections(
        serverDirectionsHashMap: HashMap<Int, DirectionOfStudy>,
        localDirectionsHashMap: HashMap<Int, DirectionOfStudy>
    ) {
        for ((id, direction) in serverDirectionsHashMap) {
            if (!localDirectionsHashMap.containsKey(id)) {
                insertDirection(direction)
            }
        }
    }

    /**
     * Обновляет направления в локальной БД, в случае, если у них изменились данные.
     * */
    private fun replaceRenewedDirections(
        serverDirectionsHashMap: HashMap<Int, DirectionOfStudy>,
        localDirectionsHashMap: HashMap<Int, DirectionOfStudy>
    ) {
        for ((id, direction) in serverDirectionsHashMap) {
            if (localDirectionsHashMap.containsKey(id) && direction.name != localDirectionsHashMap[id]!!.name) {
                insertDirection(direction)
            }
        }
    }

    /**
     * Удаляет направления из БД, в случае, если их больше нет на сервере.
     * */
    private fun deleteAbsentDirections(
        serverDirectionsHashMap: HashMap<Int, DirectionOfStudy>,
        localDirectionsHashMap: HashMap<Int, DirectionOfStudy>
    ) {
        for ((id, direction) in localDirectionsHashMap) {
            if (!serverDirectionsHashMap.containsKey(id)) {
                deleteDirection(direction)
            }
        }
    }
}
