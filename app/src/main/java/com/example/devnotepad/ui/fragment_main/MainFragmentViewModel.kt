package com.example.devnotepad.ui.fragment_main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.devnotepad.Article
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.Topic
import com.example.devnotepad.data.Repository
import com.example.devnotepad.data.local.ArticleRoomDatabase

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    /**
     * Using LiveData and caching what getAllArticles returns has several benefits:
     * - We can put an observer on the data (instead of polling for changes) and only update the
     * UI when the data actually changes.
     * - Repository is completely separated from the UI through the ViewModel.
     * */
    val allArticles: LiveData<List<Article>>
    val allTopics: LiveData<List<Topic>>
    val allDirections: LiveData<List<DirectionOfStudy>>

    init {
        val articleDao = ArticleRoomDatabase.getDatabase(application).articleDao()
        val topicDao = ArticleRoomDatabase.getDatabase(application).topicDao()
        val directionDao = ArticleRoomDatabase.getDatabase(application).directionDao()
        repository = Repository(articleDao, topicDao, directionDao)
        allArticles = repository.allArticles
        allTopics = repository.allTopics
        allDirections = repository.allDirections
    }
}
