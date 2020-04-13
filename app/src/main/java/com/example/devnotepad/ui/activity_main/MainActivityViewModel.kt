package com.example.devnotepad.ui.activity_main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.Article
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.Topic
import com.example.devnotepad.data.Repository
import com.example.devnotepad.data.local.ArticleRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

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

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     * */
    fun insert(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(article)
    }

    fun insert(topic: Topic) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(topic)
    }

    fun insert(directionOfStudy: DirectionOfStudy) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(directionOfStudy)
    }

    fun delete(directionOfStudy: DirectionOfStudy) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(directionOfStudy)
    }

    public suspend fun matchDirections(directions: List<DirectionOfStudy>) {

        if (repository.getAllDirectionsSync().isEmpty()) {
            for (direction in directions) {
                insert(direction)
            }
            return
        }

        val serverDirectionsHashMap: HashMap<Int, DirectionOfStudy> = HashMap()
        for (direction in directions) {
            serverDirectionsHashMap[direction.idFromServer] = direction
        }

        val localDirectionsHashMap: HashMap<Int, DirectionOfStudy> = HashMap()
        for (direction in repository.getAllDirectionsSync()) {
            localDirectionsHashMap[direction.idFromServer] = direction
        }

        for ((k, v) in serverDirectionsHashMap){
            if (localDirectionsHashMap.containsKey(k)) {
                if (v.name != localDirectionsHashMap[k]!!.name) {
                    insert(v)
                }
            } else {
                insert(v)
            }
        }

        for ((k, v) in localDirectionsHashMap){
            if (!serverDirectionsHashMap.containsKey(k)) {
                delete(v)
            }
        }
    }
}
