package com.example.devnotepad.ui.fragment_topics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.NotepadData
import com.example.devnotepad.Topic
import com.example.devnotepad.data.repositories.TopicsRepositoryData
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.repositories.RepositoryContractForStructureData
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import com.example.devnotepad.data.data_handlers.NotepadDataHandlerForStructure
import com.example.devnotepad.data.data_handlers.NotepadViewModelContractForStructure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopicsViewModel(application: Application) : AndroidViewModel(application),
    NotepadViewModelContractForStructure {
    override val repositoryForStructureData: RepositoryContractForStructureData
    val allTopics: LiveData<List<Topic>>
    private val devNotepadApi: DevNotepadApi
    private val notepadDataHandlerForStructure: NotepadDataHandlerForStructure
    private val topicType = "topic"

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        devNotepadApi = retrofitInstance.create(DevNotepadApi::class.java)

        val topicDao = KnowledgeRoomDatabase.getDatabase(application).topicDao()
        repositoryForStructureData = TopicsRepositoryData(topicDao)
        allTopics = repositoryForStructureData.allTopics
        notepadDataHandlerForStructure =
            NotepadDataHandlerForStructure(this, devNotepadApi)
    }

    override fun insertElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is Topic) {
                repositoryForStructureData.insertElement(notepadData)
            }
        }

    override fun deleteElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is Topic) {
                repositoryForStructureData.deleteElement(notepadData)
            }
        }

    /**
     * Осуществляет запрос на сервер для получения тем.
     * */
    override fun makeRequestForElements() {
        notepadDataHandlerForStructure.makeRequestForStructureData(topicType)
    }
}