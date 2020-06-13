package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleImage
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.repositories.ArticlesImagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticleContentViewModelForImages @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    val allArticlesImages: LiveData<List<ArticleImage>>
    val repositoryForArticlesContent: ArticlesImagesRepository

    init {
        val articleContentDao = KnowledgeRoomDatabase.getDatabase(application).articleImageDao()
        repositoryForArticlesContent = ArticlesImagesRepository(articleContentDao)
        allArticlesImages = repositoryForArticlesContent.allArticlesImages
    }

    /**TODO: temp*/
    fun updateViewHeight(webViewHeight: Int, elementId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repositoryForArticlesContent.updateViewHeight(webViewHeight, elementId)
        }
    }
}
