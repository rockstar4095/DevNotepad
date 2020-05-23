package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.devnotepad.ArticleCodeSnippet
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.data_handlers.HandlerForContentData
import com.example.devnotepad.data.local.ArticleCodeSnippetDao
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesCodeSnippetsRepository(
    private val articleCodeSnippetDao: ArticleCodeSnippetDao
) : RepositoryContractForArticlesContent,
    RepositoryContractForDynamicHeightViews {

    @Inject
    lateinit var retrofit: Retrofit

    private val devNotepadApi: DevNotepadApi
    val handlerForContentData: HandlerForContentData

    /**
     * LiveData список направлений для наблюдения из модели фрагмента.
     * */
    var allArticlesCodeSnippets: LiveData<List<ArticleCodeSnippet>> =
        articleCodeSnippetDao.getArticleCodeSnippets()

    companion object {
        const val TYPE_CODE_SNIPPET = "codeSnippet"
        var wasElementReplaced = MutableLiveData<Boolean>()
    }

    init {
        val daggerAppComponent = BaseApplication.appComponent
        daggerAppComponent.inject(this)

        devNotepadApi = retrofit.create(DevNotepadApi::class.java)

        handlerForContentData =
            HandlerForContentData(this, devNotepadApi)
    }

    override fun insertElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is ArticleCodeSnippet) {
                insertCodeSnippet(notepadData)
            }
        }
    }

    override fun deleteElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is ArticleCodeSnippet) {
                deleteCodeSnippet(notepadData)
            }
        }
    }

    override fun makeRequestForElements(parentElementId: Int) {
        handlerForContentData.makeRequestForContentData(TYPE_CODE_SNIPPET, parentElementId)
    }

    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleCodeSnippetDao.getArticleCodeSnippetsSync(parentIdFromServer)
    }

    override suspend fun updateViewHeight(viewHeight: Int, elementId: Int) {
        articleCodeSnippetDao.updateWebViewHeight(viewHeight, elementId)
    }

    override suspend fun getViewHeight(elementId: Int): Int {
        return articleCodeSnippetDao.getWebViewHeight(elementId)
    }

    override fun resetViewHeight() {
        wasElementReplaced.postValue(true)
    }

    /**
     * Инкапсулированный метод для вставки.
     * */
    private suspend fun insertCodeSnippet(articleCodeSnippet: ArticleCodeSnippet) {
        articleCodeSnippetDao.insertArticleCodeSnippet(articleCodeSnippet)
    }

    /**
     * Инкапсулированный метод для удаления.
     * */
    private suspend fun deleteCodeSnippet(articleCodeSnippet: ArticleCodeSnippet) {
        articleCodeSnippetDao.deleteArticleCodeSnippet(articleCodeSnippet)
    }
}