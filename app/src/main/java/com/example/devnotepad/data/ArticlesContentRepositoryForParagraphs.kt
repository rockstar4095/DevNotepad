package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.ArticleContentDao

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesContentRepositoryForParagraphs(
    private val articleContentDao: ArticleContentDao
): NotepadRepositoryContractForContent {

    val allArticleParagraphs: LiveData<List<ArticleParagraph>> =
        articleContentDao.getArticleParagraphs()
    
    /**
     * Синхронное получение списка направлений.
     * */
    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleContentDao.getArticleParagraphsSync(parentIdFromServer)
    }

    /**
     * Вставляет тему в БД с заменой содержимого, если она уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        articleContentDao.insertArticleParagraph(notepadData as ArticleParagraph)
    }

    /**
     * Удаляет тему из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        articleContentDao.deleteArticleParagraph(notepadData as ArticleParagraph)
    }
}