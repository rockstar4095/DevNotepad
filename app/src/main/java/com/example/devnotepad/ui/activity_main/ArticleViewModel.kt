package com.example.devnotepad.ui.activity_main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.Article
import com.example.devnotepad.data.Repository
import com.example.devnotepad.data.local.ArticleRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    /**
     * Using LiveData and caching what getAllArticles returns has several benefits:
     * - We can put an observer on the data (instead of polling for changes) and only update the
     * UI when the data actually changes.
     * - Repository is completely separated from the UI through the ViewModel.
     * */
    val allArticles: LiveData<List<Article>>

    init {
        val articleDao = ArticleRoomDatabase.getDatabase(application).articleDao()
        repository = Repository(articleDao)
        allArticles = repository.allArticles
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     * */
    fun insert(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(article)
    }
}