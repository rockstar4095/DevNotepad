package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.data_handlers.HandlerForContentData
import com.example.devnotepad.data.local.ArticleHeaderDao
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesHeadersRepository(
    private val articleHeaderDao: ArticleHeaderDao
) : RepositoryContractForArticlesContent {

    @Inject
    lateinit var retrofit: Retrofit

    private val devNotepadApi: DevNotepadApi
    private val handlerForContentData: HandlerForContentData

    /**
     * LiveData список направлений для наблюдения из модели фрагмента.
     * */
    var allArticlesHeaders: LiveData<List<ArticleHeader>> = articleHeaderDao.getArticleHeaders()

    companion object {
        public const val TYPE_HEADER = "header"
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
            if (notepadData is ArticleHeader) {
                insertHeader(notepadData)
            }
        }
    }

    override fun deleteElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is ArticleHeader) {
                deleteHeader(notepadData)
            }
        }
    }

    override fun makeRequestForElements(parentElementId: Int) {
        handlerForContentData.makeRequestForContentData(TYPE_HEADER, parentElementId)
    }

    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleHeaderDao.getArticleHeadersSync(parentIdFromServer)
    }

    /**
     * Инкапсулированный метод для вставки.
     * */
    private suspend fun insertHeader(articleHeader: ArticleHeader) {
        articleHeaderDao.insertArticleHeader(articleHeader)
    }

    /**
     * Инкапсулированный метод для удаления.
     * */
    private suspend fun deleteHeader(articleHeader: ArticleHeader) {
        articleHeaderDao.deleteArticleHeader(articleHeader)
    }
}