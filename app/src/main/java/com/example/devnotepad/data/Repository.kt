package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.Article
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.Topic
import com.example.devnotepad.data.local.ArticleDao
import com.example.devnotepad.data.local.DirectionDao
import com.example.devnotepad.data.local.TopicDao

// Declares the DAO as a private property in the constructor. Pass in the DAO instead of whole
// database, because you only need access to the DAO
class Repository(
    private val articleDao: ArticleDao,
    private val topicDao: TopicDao,
    private val directionDao: DirectionDao
) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    suspend fun insert(article: Article) {
        articleDao.insert(article)
    }

    val allTopics: LiveData<List<Topic>> = topicDao.getAllTopics()

    suspend fun insert(topic: Topic) {
        topicDao.insert(topic)
    }

    val allDirections: LiveData<List<DirectionOfStudy>> = directionDao.getAllDirections()

    suspend fun insert(direction: DirectionOfStudy) {
        directionDao.insert(direction)
    }

    suspend fun delete(direction: DirectionOfStudy) {
        directionDao.delete(direction)
    }

    suspend fun getAllDirectionsSync(): List<DirectionOfStudy> {
        return directionDao.getAllDirectionsSync()
    }
}