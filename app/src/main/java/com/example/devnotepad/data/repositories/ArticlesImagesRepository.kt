package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleImage
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.data_handlers.HandlerForContentData
import com.example.devnotepad.data.local.ArticleImageDao
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesImagesRepository(
    private val articleImageDao: ArticleImageDao
) : RepositoryContractForArticlesContent,
    RepositoryContractForDynamicHeightViews {

    @Inject
    lateinit var retrofit: Retrofit

    private val devNotepadApi: DevNotepadApi
    private val handlerForContentData: HandlerForContentData

    /**
     * LiveData список направлений для наблюдения из модели фрагмента.
     * */
    var allArticlesImages: LiveData<List<ArticleImage>> = articleImageDao.getArticleImages()

    companion object {
        public const val TYPE_IMAGE = "image"
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
            if (notepadData is ArticleImage) {
                insertImage(notepadData)
            }
        }
    }

    override fun deleteElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is ArticleImage) {
                deleteImage(notepadData)
            }
        }
    }

    override fun makeRequestForElements(parentElementId: Int) {
        handlerForContentData.makeRequestForContentData(TYPE_IMAGE, parentElementId)
    }

    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleImageDao.getArticleImagesSync(parentIdFromServer)
    }

    override suspend fun updateViewHeight(viewHeight: Int, elementId: Int) {
        articleImageDao.updateWebViewHeight(viewHeight, elementId)
    }

    override suspend fun getViewHeight(elementId: Int): Int {
        return articleImageDao.getWebViewHeight(elementId)
    }

    override fun resetViewHeight() {
        //
    }

    /**
     * Инкапсулированный метод для вставки.
     * */
    private suspend fun insertImage(articleImage: ArticleImage) {
        articleImageDao.insertArticleImage(articleImage)
    }

    /**
     * Инкапсулированный метод для удаления.
     * */
    private suspend fun deleteImage(articleImage: ArticleImage) {
        articleImageDao.deleteArticleImage(articleImage)
    }
}