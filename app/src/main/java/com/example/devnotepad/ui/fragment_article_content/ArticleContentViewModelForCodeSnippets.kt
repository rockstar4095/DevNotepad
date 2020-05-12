package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.ArticleCodeSnippet
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
import javax.inject.Inject

class ArticleContentViewModelForCodeSnippets @Inject constructor(
    application: Application
) : AndroidViewModel(application),
    NotepadViewModelContractForContent {
    override val repositoryForArticlesContent: RepositoryContractForArticlesContent
    val allArticlesCodeSnippets: LiveData<List<ArticleCodeSnippet>>
    private val notepadDataHandlerForContent: NotepadDataHandlerForContent
    private val codeSnippetType = "codeSnippet"

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        val api = retrofitInstance.create(DevNotepadApi::class.java)

        val articleContentDao = KnowledgeRoomDatabase.getDatabase(application).articleCodeSnippetDao()

        repositoryForArticlesContent = ArticlesCodeSnippetsRepository(articleContentDao)
        allArticlesCodeSnippets = repositoryForArticlesContent.allArticlesCodeSnippets
        notepadDataHandlerForContent = NotepadDataHandlerForContent(this, api)
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
}
