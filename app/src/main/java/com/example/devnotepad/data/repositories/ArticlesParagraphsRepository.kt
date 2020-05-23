package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.data_handlers.HandlerForContentData
import com.example.devnotepad.data.local.ArticleParagraphDao
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesParagraphsRepository(
    private val articleParagraphDao: ArticleParagraphDao
) : RepositoryContractForArticlesContent {

    @Inject
    lateinit var retrofit: Retrofit

    private val devNotepadApi: DevNotepadApi
    private val handlerForContentData: HandlerForContentData

    /**
     * LiveData список для наблюдения из модели фрагмента.
     * */
    var allArticlesParagraphs: LiveData<List<ArticleParagraph>> =
        articleParagraphDao.getArticleParagraphs()

    companion object {
        public const val TYPE_PARAGRAPH = "paragraph"
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
            if (notepadData is ArticleParagraph) {
                insertParagraph(notepadData)
            }
        }
    }

    override fun deleteElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is ArticleParagraph) {
                deleteParagraph(notepadData)
            }
        }
    }

    override fun makeRequestForElements(parentElementId: Int) {
        handlerForContentData.makeRequestForContentData(TYPE_PARAGRAPH, parentElementId)
    }

    override suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData> {
        return articleParagraphDao.getArticleParagraphsSync(parentIdFromServer)
    }

    /**
     * Инкапсулированный метод для вставки.
     * */
    private suspend fun insertParagraph(articleParagraph: ArticleParagraph) {
        articleParagraphDao.insertArticleParagraph(articleParagraph)
    }

    /**
     * Инкапсулированный метод для удаления.
     * */
    private suspend fun deleteParagraph(articleParagraph: ArticleParagraph) {
        articleParagraphDao.deleteArticleParagraph(articleParagraph)
    }
}