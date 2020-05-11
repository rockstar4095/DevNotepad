package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.ArticleParagraphDao

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesParagraphsRepository(
    private val articleParagraphDao: ArticleParagraphDao
) : RepositoryContractForArticlesContent {

    val allArticleParagraphs: LiveData<List<ArticleParagraph>> =
        articleParagraphDao.getArticleParagraphs()

    /**
     * Синхронное получение списка параграфов.
     * */
    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleParagraphDao.getArticleParagraphsSync(parentIdFromServer)
    }

    /**
     * Вставляет параграф в БД с заменой содержимого, если он уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        articleParagraphDao.insertArticleParagraph(notepadData as ArticleParagraph)
    }

    /**
     * Удаляет параграф из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        articleParagraphDao.deleteArticleParagraph(notepadData as ArticleParagraph)
    }
}