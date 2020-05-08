package com.example.devnotepad.ui.fragment_topics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.Topic
import com.example.devnotepad.data.TopicsRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopicsViewModel(application: Application) : AndroidViewModel(application) {
    private val topicsRepository: TopicsRepository
    val allTopics: LiveData<List<Topic>>
    private val api: DevNotepadApi

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        api = retrofitInstance.create(DevNotepadApi::class.java)

        val topicDao = KnowledgeRoomDatabase.getDatabase(application).topicDao()
        topicsRepository = TopicsRepository(topicDao)
        allTopics = topicsRepository.allTopics
    }

    private fun insertTopic(topic: Topic) =
        viewModelScope.launch(Dispatchers.IO) {
            topicsRepository.insertTopic(topic)
        }

    private fun deleteTopic(topic: Topic) =
        viewModelScope.launch(Dispatchers.IO) {
            topicsRepository.deleteTopic(topic)
        }

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    fun makeRequestForTopics() {
        api.getTopics().enqueue(object: Callback<List<Topic>> {
            override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                CoroutineScope(Dispatchers.IO).launch {
                    handleServerTopics(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                println("response unsuccessful: $t")
            }
        })
    }

    /**
     * Обрабатывает направления, полученные от сервера, с направлениями, хранящимися в локальной БД.
     * */
    private suspend fun handleServerTopics(topicsFromServer: List<Topic>) {

        // Проверка на отсутствие данных в таблице.
        if (isTableEmpty()) {
            // Вставка направлений в пустую таблицу.
            insertTopics(topicsFromServer)
            // Выход из метода.
            return
        }

        // Сравнение данных с сервера с локальными.
        matchTopicsFromServerAndLocal(topicsFromServer)
    }

    /**
     * Проверяет таблицу с направлениями на наличие данных.
     * */
    private suspend fun isTableEmpty(): Boolean {
        return topicsRepository.getAllTopicsSync().isEmpty()
    }

    /**
     * Вставляет список направлений в таблицу.
     * */
    private fun insertTopics(topics: List<Topic>) {
        for (topic in topics) {
            insertTopic(topic)
        }
    }

    /**
     * Сравнивает направления с сервера с локальными и вызывает методы вставки/обновления/удаления.
     * */
    private suspend fun matchTopicsFromServerAndLocal(topicsFromServer: List<Topic>) {

        val serverTopicsHashMap: HashMap<Int, Topic> = HashMap()
        val localTopicsHashMap: HashMap<Int, Topic> = HashMap()

        for (topic in topicsFromServer) {
            serverTopicsHashMap[topic.idFromServer] = topic
        }

        for (topic in topicsRepository.getAllTopicsSync()) {
            localTopicsHashMap[topic.idFromServer] = topic
        }

        insertNewTopics(serverTopicsHashMap, localTopicsHashMap)
        replaceRenewedTopics(serverTopicsHashMap, localTopicsHashMap)
        deleteAbsentTopics(serverTopicsHashMap, localTopicsHashMap)
    }

    /**
     * Вставляет новые направления в локальную БД.
     * */
    private fun insertNewTopics(
        serverTopicsHashMap: HashMap<Int, Topic>,
        localTopicsHashMap: HashMap<Int, Topic>
    ) {
        for ((id, topic) in serverTopicsHashMap) {
            if (!localTopicsHashMap.containsKey(id)) {
                insertTopic(topic)
            }
        }
    }

    /**
     * Обновляет направления в локальной БД, в случае, если у них изменились данные.
     * */
    private fun replaceRenewedTopics(
        serverTopicsHashMap: HashMap<Int, Topic>,
        localTopicsHashMap: HashMap<Int, Topic>
    ) {
        for ((id, topic) in serverTopicsHashMap) {
            if (localTopicsHashMap.containsKey(id)
                && topic.timeWhenDataChanged != localTopicsHashMap[id]!!.timeWhenDataChanged) {
                insertTopic(topic)
            }
        }
    }

    /**
     * Удаляет направления из БД, в случае, если их больше нет на сервере.
     * */
    private fun deleteAbsentTopics(
        serverTopicsHashMap: HashMap<Int, Topic>,
        localTopicsHashMap: HashMap<Int, Topic>
    ) {
        for ((id, topic) in localTopicsHashMap) {
            if (!serverTopicsHashMap.containsKey(id)) {
                deleteTopic(topic)
            }
        }
    }
}