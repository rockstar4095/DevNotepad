package com.example.devnotepad.ui.fragment_directions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.DirectionsRepository
import com.example.devnotepad.data.NotepadRepositoryContractForStructure
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import com.example.devnotepad.ui.NotepadDataHandlerForStructure
import com.example.devnotepad.ui.NotepadViewModelContractForStructure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DirectionsViewModel(application: Application) : AndroidViewModel(application),
    NotepadViewModelContractForStructure {
    override val notepadRepository: NotepadRepositoryContractForStructure
    val allDirections: LiveData<List<DirectionOfStudy>>
    private val api: DevNotepadApi
    private val notepadDataHandler: NotepadDataHandlerForStructure
    private val directionType = "direction"

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        api = retrofitInstance.create(DevNotepadApi::class.java)

        val directionDao = KnowledgeRoomDatabase.getDatabase(application).directionDao()
        notepadRepository = DirectionsRepository(directionDao)
        allDirections = notepadRepository.allDirections
        notepadDataHandler = NotepadDataHandlerForStructure(this, api)
    }

    override fun insertElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is DirectionOfStudy) {
                notepadRepository.insertElement(notepadData)
            }
        }

    override fun deleteElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is DirectionOfStudy) {
                notepadRepository.deleteElement(notepadData)
            }
        }

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    override fun makeRequestForElements() {
        notepadDataHandler.makeRequestForStructureData(directionType)
    }
}
