package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.Article
import com.example.devnotepad.data.local.ArticleDao

// Declares the DAO as a private property in the constructor. Pass in the DAO instead of whole
// database, because you only need access to the DAO
class Repository(private val articleDao: ArticleDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    suspend fun insert(article: Article) {
        articleDao.insert(article)
    }
}