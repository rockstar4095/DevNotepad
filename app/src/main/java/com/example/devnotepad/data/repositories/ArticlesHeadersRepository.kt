package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.ArticleHeaderDao

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesHeadersRepository(
    private val articleHeaderDao: ArticleHeaderDao
) : RepositoryContractForArticlesContent {

    val allArticlesHeaders: LiveData<List<ArticleHeader>> =
        articleHeaderDao.getArticleHeaders()

    /**
     * Синхронное получение списка.
     * */
    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleHeaderDao.getArticleHeadersSync(parentIdFromServer)
    }

    /**
     * Вставляет заголовок в БД с заменой содержимого, если он уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        articleHeaderDao.insertArticleHeader(notepadData as ArticleHeader)
    }

    /**
     * Удаляет заголовок из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        articleHeaderDao.deleteArticleHeader(notepadData as ArticleHeader)
    }
}