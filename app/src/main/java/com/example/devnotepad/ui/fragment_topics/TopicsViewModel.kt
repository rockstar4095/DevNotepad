package com.example.devnotepad.ui.fragment_topics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.devnotepad.Topic
import com.example.devnotepad.data.repositories.TopicsRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import javax.inject.Inject

class TopicsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val allTopics: LiveData<List<Topic>>
    val repositoryForStructureData: TopicsRepository

    init {
        val topicDao = KnowledgeRoomDatabase.getDatabase(application).topicDao()
        repositoryForStructureData = TopicsRepository(topicDao)
        allTopics = repositoryForStructureData.allTopics
    }
}