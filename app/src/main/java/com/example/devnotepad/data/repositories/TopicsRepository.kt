package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.NotepadData
import com.example.devnotepad.Topic
import com.example.devnotepad.data.data_handlers.HandlerForStructureData
import com.example.devnotepad.data.local.TopicDao
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class TopicsRepository(private val topicDao: TopicDao) :
    RepositoryContractForStructureData {

    @Inject
    lateinit var retrofit: Retrofit

    private val topicType = "topic"
    private val devNotepadApi: DevNotepadApi
    private val handlerForStructureData: HandlerForStructureData

    /**
     * LiveData список тем для наблюдения из модели фрагмента.
     * */
    var allTopics: LiveData<List<Topic>> = topicDao.getAllTopics()

    init {
        val daggerAppComponent = BaseApplication.appComponent
        daggerAppComponent.inject(this)

        devNotepadApi = retrofit.create(DevNotepadApi::class.java)

        handlerForStructureData =
            HandlerForStructureData(this, devNotepadApi)
    }

    override fun insertElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is Topic) {
                insertTopic(notepadData)
            }
        }
    }

    override fun deleteElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is Topic) {
                deleteTopic(notepadData)
            }
        }
    }

    override fun makeRequestForElements() {
        handlerForStructureData.makeRequestForStructureData(topicType)
    }

    override suspend fun getAllElementsSync(): List<NotepadData> {
        return topicDao.getAllTopicsSync()
    }

    /**
     * Инкапсулированный метод для вставки темы.
     * */
    private suspend fun insertTopic(topic: Topic) {
        topicDao.insertTopic(topic)
    }

    /**
     * Инкапсулированный метод для удаления темы.
     * */
    private suspend fun deleteTopic(topic: Topic) {
        topicDao.deleteTopic(topic)
    }
}