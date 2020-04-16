package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.Article
import com.example.devnotepad.data.local.ArticleDao

class ArticlesRepository(private val articleDao: ArticleDao) {

    /**
     * LiveData список статей для наблюдения из модели фрагмента.
     * */
    val allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    /**
     * Синхронное получение списка статей.
     * */
    suspend fun getAllArticlesSync(): List<Article> {
        return articleDao.getAllArticlesSync()
    }

    /**
     * Вставляет статью в БД с заменой содержимого, если она уже существует.
     * */
    suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    /**
     * Удаляет статью из БД.
     * */
    suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }
}