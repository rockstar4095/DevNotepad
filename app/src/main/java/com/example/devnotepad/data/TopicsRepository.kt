package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.Topic
import com.example.devnotepad.data.local.TopicDao

class TopicsRepository(private val topicDao: TopicDao) {

    /**
     * LiveData список тем для наблюдения из модели фрагмента.
     * */
    val allTopics: LiveData<List<Topic>> = topicDao.getAllTopics()

    /**
     * Синхронное получение списка направлений.
     * */
    suspend fun getAllTopicsSync(): List<Topic> {
        return topicDao.getAllTopicsSync()
    }

    /**
     * Вставляет тему в БД с заменой содержимого, если она уже существует.
     * */
    suspend fun insertTopic(topic: Topic) {
        topicDao.insertTopic(topic)
    }

    /**
     * Удаляет тему из БД.
     * */
    suspend fun deleteTopic(topic: Topic) {
        topicDao.deleteTopic(topic)
    }
}