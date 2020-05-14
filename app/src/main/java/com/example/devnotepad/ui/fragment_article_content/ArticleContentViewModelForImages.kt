package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.ArticleImage
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.data_handlers.NotepadDataHandlerForContent
import com.example.devnotepad.data.data_handlers.NotepadViewModelContractForContent
import com.example.devnotepad.data.repositories.ArticlesImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import javax.inject.Inject

class ArticleContentViewModelForImages @Inject constructor(
    application: Application
) : AndroidViewModel(application),
    NotepadViewModelContractForContent {
    override val repositoryForArticlesContent: ArticlesImagesRepository
    val allArticlesImages: LiveData<List<ArticleImage>>
    private val devNotepadApi: DevNotepadApi
    private val notepadDataHandlerForContent: NotepadDataHandlerForContent
    private val imageType = "image"

    @Inject
    lateinit var retrofit: Retrofit

    init {
        val daggerAppComponent = BaseApplication.appComponent
        daggerAppComponent.inject(this)

        devNotepadApi = retrofit.create(DevNotepadApi::class.java)

        val articleContentDao = KnowledgeRoomDatabase.getDatabase(application).articleImageDao()

        repositoryForArticlesContent = ArticlesImagesRepository(articleContentDao)
        allArticlesImages = repositoryForArticlesContent.allArticlesImages
        notepadDataHandlerForContent = NotepadDataHandlerForContent(this, devNotepadApi)
    }

    /**
     * Методы вставки и удаления находятся в данном классе по причине того, что здесь есть
     * доступ к viewModelScope.
     * */
    override fun insertElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is ArticleImage) {
                repositoryForArticlesContent.insertElement(notepadData)
            }
        }

    override fun deleteElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is ArticleImage) {
                repositoryForArticlesContent.deleteElement(notepadData)
            }
        }

    /**
     * Вызывает методы для запроса на сервер у всех классов-обработчиков элементов статьи.
     * */
    override fun makeRequestForElements(parentElementId: Int) {
        notepadDataHandlerForContent.makeRequestForContentData(imageType, parentElementId)
    }

    /**
     * Обновляет поле webViewHeight.
     * */
    fun updateWebViewHeight(webViewHeight: Int, articleImageId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryForArticlesContent.updateWebViewHeight(webViewHeight, articleImageId)
        }
    }

    /**
     * Получает значение webViewHeight.
     * */
    fun getWebViewHeight(articleImageId: Int): Int {
        return runBlocking {
            repositoryForArticlesContent.getWebViewHeight(articleImageId)
        }
    }
}
