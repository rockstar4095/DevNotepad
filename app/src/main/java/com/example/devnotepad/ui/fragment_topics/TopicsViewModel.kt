package com.example.devnotepad.ui.fragment_topics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.NotepadData
import com.example.devnotepad.Topic
import com.example.devnotepad.data.NotepadRepositoryContractForStructure
import com.example.devnotepad.data.TopicsRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import com.example.devnotepad.ui.NotepadDataHandlerForStructure
import com.example.devnotepad.ui.NotepadViewModelContractForStructure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopicsViewModel(application: Application) : AndroidViewModel(application),
    NotepadViewModelContractForStructure {
    override val notepadRepository: NotepadRepositoryContractForStructure
    val allTopics: LiveData<List<Topic>>
    private val api: DevNotepadApi
    private val notepadDataHandler: NotepadDataHandlerForStructure
    private val topicType = "topic"

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        api = retrofitInstance.create(DevNotepadApi::class.java)

        val topicDao = KnowledgeRoomDatabase.getDatabase(application).topicDao()
        notepadRepository = TopicsRepository(topicDao)
        allTopics = notepadRepository.allTopics
        notepadDataHandler = NotepadDataHandlerForStructure(this, api)
    }

    override fun insertElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is Topic) {
                notepadRepository.insertElement(notepadData as Topic)
            }
        }

    override fun deleteElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is Topic) {
                notepadRepository.deleteElement(notepadData)
            }
        }

    /**
     * Осуществляет запрос на сервер для получения тем.
     * */
    override fun makeRequestForElements() {
        notepadDataHandler.makeRequestForStructureData(topicType)
    }
}