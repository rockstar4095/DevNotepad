package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleCodeSnippet
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.repositories.ArticlesCodeSnippetsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticleContentViewModelForCodeSnippets @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    val allArticlesCodeSnippets: LiveData<List<ArticleCodeSnippet>>
    val repositoryForArticlesContent: ArticlesCodeSnippetsRepository

    init {
        val articleContentDao =
            KnowledgeRoomDatabase.getDatabase(application).articleCodeSnippetDao()
        repositoryForArticlesContent = ArticlesCodeSnippetsRepository(articleContentDao)
        allArticlesCodeSnippets = repositoryForArticlesContent.allArticlesCodeSnippets
    }

    /**TODO: temp*/
    fun updateViewHeight(webViewHeight: Int, elementId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repositoryForArticlesContent.updateViewHeight(webViewHeight, elementId)
        }
    }
}
