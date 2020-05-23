package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.Article
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.data_handlers.HandlerForStructureData
import com.example.devnotepad.data.local.ArticleDao
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class ArticlesRepository(private val articleDao: ArticleDao) :
    RepositoryContractForStructureData {

    @Inject
    lateinit var retrofit: Retrofit

    private val articleType = "article"
    private val devNotepadApi: DevNotepadApi
    private val handlerForStructureData: HandlerForStructureData

    /**
     * LiveData список статей для наблюдения из модели фрагмента.
     * */
    var allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    init {
        val daggerAppComponent = BaseApplication.appComponent
        daggerAppComponent.inject(this)

        devNotepadApi = retrofit.create(DevNotepadApi::class.java)

        handlerForStructureData =
            HandlerForStructureData(this, devNotepadApi)
    }

    override fun insertElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is Article) {
                insertArticle(notepadData)
            }
        }
    }

    override fun deleteElement(notepadData: NotepadData) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notepadData is Article) {
                deleteArticle(notepadData)
            }
        }
    }

    override fun makeRequestForElements() {
        handlerForStructureData.makeRequestForStructureData(articleType)
    }

    override suspend fun getAllElementsSync(): List<NotepadData> {
        return articleDao.getAllArticlesSync()
    }

    /**
     * Инкапсулированный метод для вставки статьи.
     * */
    private suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    /**
     * Инкапсулированный метод для удаления статьи.
     * */
    private suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }
}