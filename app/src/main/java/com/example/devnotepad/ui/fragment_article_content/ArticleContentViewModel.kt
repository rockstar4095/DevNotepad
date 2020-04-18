package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.Article
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.data.ArticlesContentRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleContentViewModel(
    application: Application,
    internal val article: Article
) : AndroidViewModel(application) {
    internal val articlesContentRepository: ArticlesContentRepository
    val allArticleHeaders: LiveData<List<ArticleHeader>>
    val allArticleParagraphs: LiveData<List<ArticleParagraph>>
    internal val api: DevNotepadApi
    private val headersPart: HeadersPart
    private val paragraphsPart: ParagraphsPart
    private var contentPieces: MutableLiveData<ArrayList<Any>>

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        api = retrofitInstance.create(DevNotepadApi::class.java)

        val articleContentDao = KnowledgeRoomDatabase.getDatabase(application).articleContentDao()

        articlesContentRepository =
            ArticlesContentRepository(articleContentDao, article.idFromServer)

        allArticleHeaders = articlesContentRepository.allArticleHeaders
        allArticleParagraphs = articlesContentRepository.allArticleParagraphs

        headersPart = HeadersPart(this)
        paragraphsPart = ParagraphsPart(this)
        contentPieces = MutableLiveData()
    }

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

    fun makeRequestForContent() {
        headersPart.makeRequestForHeaders()
        paragraphsPart.makeRequestForParagraphs()
    }
}
