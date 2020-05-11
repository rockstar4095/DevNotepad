package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.Article
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.ArticleDao

class ArticlesRepositoryData(private val articleDao: ArticleDao) :
    RepositoryContractForStructureData {

    /**
     * LiveData список статей для наблюдения из модели фрагмента.
     * */
    val allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    /**
     * Синхронное получение списка статей.
     * */
    override suspend fun getAllElementsSync(): List<NotepadData> {
        return articleDao.getAllArticlesSync()
    }

    /**
     * Вставляет статью в БД с заменой содержимого, если она уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        articleDao.insertArticle(notepadData as Article)
    }

    /**
     * Удаляет статью из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        articleDao.deleteArticle(notepadData as Article)
    }
}