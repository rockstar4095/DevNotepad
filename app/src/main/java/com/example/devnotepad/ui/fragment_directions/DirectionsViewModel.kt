package com.example.devnotepad.ui.fragment_directions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.repositories.DirectionsRepositoryData
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.repositories.RepositoryContractForStructureData
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import com.example.devnotepad.data.data_handlers.NotepadDataHandlerForStructure
import com.example.devnotepad.data.data_handlers.NotepadViewModelContractForStructure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DirectionsViewModel(application: Application) : AndroidViewModel(application),
    NotepadViewModelContractForStructure {
    override val repositoryForStructureData: RepositoryContractForStructureData
    val allDirections: LiveData<List<DirectionOfStudy>>
    private val devNotepadApi: DevNotepadApi
    private val notepadDataHandlerForStructure: NotepadDataHandlerForStructure
    private val directionType = "direction"

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        devNotepadApi = retrofitInstance.create(DevNotepadApi::class.java)

        val directionDao = KnowledgeRoomDatabase.getDatabase(application).directionDao()
        repositoryForStructureData = DirectionsRepositoryData(directionDao)
        allDirections = repositoryForStructureData.allDirections
        notepadDataHandlerForStructure =
            NotepadDataHandlerForStructure(
                this,
                devNotepadApi
            )
    }

    override fun insertElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is DirectionOfStudy) {
                repositoryForStructureData.insertElement(notepadData)
            }
        }

    override fun deleteElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is DirectionOfStudy) {
                repositoryForStructureData.deleteElement(notepadData)
            }
        }

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    override fun makeRequestForElements() {
        notepadDataHandlerForStructure.makeRequestForStructureData(directionType)
    }
}
