package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.ArticleContentDao

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesContentRepositoryForHeaders(
    private val articleContentDao: ArticleContentDao
): NotepadRepositoryContractForContent {

    val allArticleHeaders: LiveData<List<ArticleHeader>> =
        articleContentDao.getArticleHeaders()

    /**
     * Синхронное получение списка направлений.
     * */
    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleContentDao.getArticleHeadersSync(parentIdFromServer)
    }

    /**
     * Вставляет тему в БД с заменой содержимого, если она уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        articleContentDao.insertArticleHeader(notepadData as ArticleHeader)
    }

    /**
     * Удаляет тему из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        articleContentDao.deleteArticleHeader(notepadData as ArticleHeader)
    }
}