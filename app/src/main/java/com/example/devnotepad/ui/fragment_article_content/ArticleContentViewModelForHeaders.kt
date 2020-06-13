package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.repositories.ArticlesHeadersRepository
import javax.inject.Inject

class ArticleContentViewModelForHeaders @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    val allArticlesHeaders: LiveData<List<ArticleHeader>>
    val repositoryForArticlesContent: ArticlesHeadersRepository

    init {
        val articleHeaderDao = KnowledgeRoomDatabase.getDatabase(application).articleHeaderDao()
        repositoryForArticlesContent = ArticlesHeadersRepository(articleHeaderDao)
        allArticlesHeaders = repositoryForArticlesContent.allArticlesHeaders
    }
}