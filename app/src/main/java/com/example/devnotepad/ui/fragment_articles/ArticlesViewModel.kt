package com.example.devnotepad.ui.fragment_articles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.Article
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.repositories.ArticlesRepositoryData
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.repositories.RepositoryContractForStructureData
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import com.example.devnotepad.data.data_handlers.NotepadDataHandlerForStructure
import com.example.devnotepad.data.data_handlers.NotepadViewModelContractForStructure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticlesViewModel(application: Application) : AndroidViewModel(application),
    NotepadViewModelContractForStructure {
    override val repositoryForStructureData: RepositoryContractForStructureData
    val allArticles: LiveData<List<Article>>
    private val devNotepadApi: DevNotepadApi
    private val notepadDataHandlerForStructure: NotepadDataHandlerForStructure
    private val articleType = "article"

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        devNotepadApi = retrofitInstance.create(DevNotepadApi::class.java)

        val articleDao = KnowledgeRoomDatabase.getDatabase(application).articleDao()
        repositoryForStructureData = ArticlesRepositoryData(articleDao)
        allArticles = repositoryForStructureData.allArticles
        notepadDataHandlerForStructure =
            NotepadDataHandlerForStructure(this, devNotepadApi)
    }

    override fun insertElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is Article) {
                repositoryForStructureData.insertElement(notepadData)
            }
        }

    override fun deleteElement(notepadData: NotepadData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (notepadData is Article) {
                repositoryForStructureData.deleteElement(notepadData)
            }
        }

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    override fun makeRequestForElements() {
        notepadDataHandlerForStructure.makeRequestForStructureData(articleType)
    }
}