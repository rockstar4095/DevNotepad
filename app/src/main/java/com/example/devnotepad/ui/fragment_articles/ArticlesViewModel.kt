package com.example.devnotepad.ui.fragment_articles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.Article
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.ArticlesRepository
import com.example.devnotepad.data.NotepadRepositoryContractForStructure
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import com.example.devnotepad.ui.NotepadDataHandlerForStructure
import com.example.devnotepad.ui.NotepadViewModelContractForStructure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticlesViewModel(application: Application) : AndroidViewModel(application),
    NotepadViewModelContractForStructure {
    override val notepadRepository: NotepadRepositoryContractForStructure
    val allArticles: LiveData<List<Article>>
    private val api: DevNotepadApi
    private val notepadDataHandler: NotepadDataHandlerForStructure
    private val articleType = "article"

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        api = retrofitInstance.create(DevNotepadApi::class.java)

        val articleDao = KnowledgeRoomDatabase.getDatabase(application).articleDao()
        notepadRepository = ArticlesRepository(articleDao)
        allArticles = notepadRepository.allArticles
        notepadDataHandler = NotepadDataHandlerForStructure(this, api)
    }

    override fun insertElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is Article) {
                notepadRepository.insertElement(notepadData)
            }
        }

    override fun deleteElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is Article) {
                notepadRepository.deleteElement(notepadData)
            }
        }

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    override fun makeRequestForElements() {
        notepadDataHandler.makeRequestForStructureData(articleType)
    }
}