package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.Article
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.ArticleDao

class ArticlesRepository(private val articleDao: ArticleDao): NotepadRepositoryContractForStructure {

    /**
     * LiveData список статей для наблюдения из модели фрагмента.
     * */
    val allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    /**
     * Синхронное получение списка направлений.
     * */
    override suspend fun getAllElementsSync(): List<NotepadData> {
        return articleDao.getAllArticlesSync()
    }

    /**
     * Вставляет тему в БД с заменой содержимого, если она уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        articleDao.insertArticle(notepadData as Article)
    }

    /**
     * Удаляет тему из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        articleDao.deleteArticle(notepadData as Article)
    }
}