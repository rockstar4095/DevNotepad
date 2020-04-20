package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.data.ArticlesContentRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleContentViewModel(
    application: Application
) : AndroidViewModel(application) {
    internal val articlesContentRepository: ArticlesContentRepository
    val allArticlesHeaders: LiveData<List<ArticleHeader>>
    val allArticlesParagraphs: LiveData<List<ArticleParagraph>>

    /**
     * Классы для обработки элементов статьи. В них происходит запрос на сервер и работа с БД.
     * Созданы для того, чтобы не писать весь код в данном классе.
     * */
    private val headersPart: HeadersPart
    private val paragraphsPart: ParagraphsPart

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        val api = retrofitInstance.create(DevNotepadApi::class.java)

        val articleContentDao = KnowledgeRoomDatabase.getDatabase(application).articleContentDao()

        articlesContentRepository =
            ArticlesContentRepository(articleContentDao)

        allArticlesHeaders = articlesContentRepository.allArticleHeaders
        allArticlesParagraphs = articlesContentRepository.allArticleParagraphs

        headersPart = HeadersPart(this, api)
        paragraphsPart = ParagraphsPart(this, api)
    }

    /**
     * Методы вставки и удаления находятся в данном классе по причине того, что здесь есть
     * доступ к viewModelScope.
     * */
    internal fun insertHeader(articleHeader: ArticleHeader) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesContentRepository.insertArticleHeader(articleHeader)
        }
    }

    internal fun deleteHeader(articleHeader: ArticleHeader) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesContentRepository.deleteArticleHeader(articleHeader)
        }
    }

    internal fun insertParagraph(articleParagraph: ArticleParagraph) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesContentRepository.insertArticleParagraph(articleParagraph)
        }
    }

    internal fun deleteParagraph(articleParagraph: ArticleParagraph) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesContentRepository.deleteArticleParagraph(articleParagraph)
        }
    }

    /**
     * Вызывает методы для запроса на сервер у всех классов-обработчиков элементов статьи.
     * */
    fun makeRequestForContent(articleId: Int) {
        headersPart.makeRequestForHeaders(articleId)
        paragraphsPart.makeRequestForParagraphs(articleId)
    }
}
