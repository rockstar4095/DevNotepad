package com.example.devnotepad.ui.fragment_articles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.devnotepad.Article
import com.example.devnotepad.data.repositories.ArticlesRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import javax.inject.Inject

class ArticlesViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val allArticles: LiveData<List<Article>>
    val repositoryForStructureData: ArticlesRepository

    init {
        val articleDao = KnowledgeRoomDatabase.getDatabase(application).articleDao()
        repositoryForStructureData = ArticlesRepository(articleDao)
        allArticles = repositoryForStructureData.allArticles
    }
}