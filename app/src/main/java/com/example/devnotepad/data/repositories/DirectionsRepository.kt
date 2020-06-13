package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.data_handlers.HandlerForStructureData
import com.example.devnotepad.data.local.DirectionDao
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

class DirectionsRepository(private val directionDao: DirectionDao) :
    RepositoryContractForStructureData {

    @Inject
    lateinit var retrofit: Retrofit

    private val directionType = "direction"
    private val devNotepadApi: DevNotepadApi
    private val handlerForStructureData: HandlerForStructureData

    /**
     * LiveData список направлений для наблюдения из модели фрагмента.
     * */
    var allDirections: LiveData<List<DirectionOfStudy>> = directionDao.getAllDirections()

    init {
        val daggerAppComponent = BaseApplication.appComponent
        daggerAppComponent.inject(this)

        devNotepadApi = retrofit.create(DevNotepadApi::class.java)

        handlerForStructureData =
            HandlerForStructureData(this, devNotepadApi)
    }

    override fun insertElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is DirectionOfStudy) {
                insertDirection(notepadData)
            }
        }
    }

    override fun deleteElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is DirectionOfStudy) {
                deleteDirection(notepadData)
            }
        }
    }

    override fun makeRequestForElements() {
        handlerForStructureData.makeRequestForStructureData(directionType)
    }

    override suspend fun getAllElementsSync(): List<NotepadData> {
        return directionDao.getAllDirectionsSync()
    }

    /**
     * Инкапсулированный метод для вставки.
     * */
    private suspend fun insertDirection(directionOfStudy: DirectionOfStudy) {
        directionDao.insertDirection(directionOfStudy)
    }

    /**
     * Инкапсулированный метод для удаления.
     * */
    private suspend fun deleteDirection(directionOfStudy: DirectionOfStudy) {
        directionDao.deleteDirection(directionOfStudy)
    }
}