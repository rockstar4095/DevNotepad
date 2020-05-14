package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleImage
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.ArticleImageDao

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesImagesRepository(
    private val articleImageDao: ArticleImageDao
) : RepositoryContractForArticlesContent {

    val allArticlesImages: LiveData<List<ArticleImage>> =
        articleImageDao.getArticleImages()

    /**
     * Синхронное получение списка параграфов.
     * */
    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleImageDao.getArticleImagesSync(parentIdFromServer)
    }

    /**
     * Вставляет параграф в БД с заменой содержимого, если он уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        articleImageDao.insertArticleImage(notepadData as ArticleImage)
    }

    /**
     * Удаляет параграф из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        articleImageDao.deleteArticleImage(notepadData as ArticleImage)
    }

    /**
     * Обновляет поле webViewHeight.
     * */
    suspend fun updateWebViewHeight(webViewHeight: Int, articleImageId: Int) {
        articleImageDao.updateWebViewHeight(webViewHeight, articleImageId)
    }

    /**
     * Получает значение webViewHeight.
     * */
    suspend fun getWebViewHeight(articleImageId: Int): Int {
        return articleImageDao.getWebViewHeight(articleImageId)
    }
}