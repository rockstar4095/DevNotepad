package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.ArticleCodeSnippet
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.repositories.RepositoryContractForArticlesContent
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import com.example.devnotepad.data.data_handlers.NotepadDataHandlerForContent
import com.example.devnotepad.data.data_handlers.NotepadViewModelContractForContent
import com.example.devnotepad.data.repositories.ArticlesCodeSnippetsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import javax.inject.Inject

class ArticleContentViewModelForCodeSnippets @Inject constructor(
    application: Application
) : AndroidViewModel(application),
    NotepadViewModelContractForContent {
    override val repositoryForArticlesContent: ArticlesCodeSnippetsRepository
    val allArticlesCodeSnippets: LiveData<List<ArticleCodeSnippet>>
    private val devNotepadApi: DevNotepadApi
    private val notepadDataHandlerForContent: NotepadDataHandlerForContent
    private val codeSnippetType = "codeSnippet"

    @Inject
    lateinit var retrofit: Retrofit

    init {
        val daggerAppComponent = BaseApplication.appComponent
        daggerAppComponent.inject(this)

        devNotepadApi = retrofit.create(DevNotepadApi::class.java)

        val articleContentDao = KnowledgeRoomDatabase.getDatabase(application).articleCodeSnippetDao()

        repositoryForArticlesContent = ArticlesCodeSnippetsRepository(articleContentDao)
        allArticlesCodeSnippets = repositoryForArticlesContent.allArticlesCodeSnippets
        notepadDataHandlerForContent = NotepadDataHandlerForContent(this, devNotepadApi)
    }

    /**
     * Методы вставки и удаления находятся в данном классе по причине того, что здесь есть
     * доступ к viewModelScope.
     * */
    override fun insertElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is ArticleCodeSnippet) {
                repositoryForArticlesContent.insertElement(notepadData)
            }
        }

    override fun deleteElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is ArticleCodeSnippet) {
                repositoryForArticlesContent.deleteElement(notepadData)
            }
        }

    /**
     * Вызывает методы для запроса на сервер у всех классов-обработчиков элементов статьи.
     * */
    override fun makeRequestForElements(parentElementId: Int) {
        notepadDataHandlerForContent.makeRequestForContentData(codeSnippetType, parentElementId)
    }

    /**
     * Обновляет поле webViewHeight.
     * */
    fun updateWebViewHeight(webViewHeight: Int, articleCodeSnippetId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryForArticlesContent.updateWebViewHeight(webViewHeight, articleCodeSnippetId)
        }
    }

    /**
     * Получает значение webViewHeight.
     * */
    fun getWebViewHeight(articleCodeSnippetId: Int): Int {
        return runBlocking {
            repositoryForArticlesContent.getWebViewHeight(articleCodeSnippetId)
        }
    }
}
