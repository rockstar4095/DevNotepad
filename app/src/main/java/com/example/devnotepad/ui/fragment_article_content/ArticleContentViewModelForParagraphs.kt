package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.repositories.ArticlesParagraphsRepository
import javax.inject.Inject

class ArticleContentViewModelForParagraphs @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    val allArticlesParagraphs: LiveData<List<ArticleParagraph>>
    val repositoryForArticlesContent: ArticlesParagraphsRepository

    init {
        val articleContentDao = KnowledgeRoomDatabase.getDatabase(application).articleParagraphDao()
        repositoryForArticlesContent = ArticlesParagraphsRepository(articleContentDao)
        allArticlesParagraphs = repositoryForArticlesContent.allArticlesParagraphs
    }
}