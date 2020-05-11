package com.example.devnotepad.data.data_handlers

import com.example.devnotepad.data.data_handlers.NotePadViewModelContract
import com.example.devnotepad.data.repositories.RepositoryContractForStructureData

interface NotepadViewModelContractForStructure :
    NotePadViewModelContract {
    val repositoryForStructureData: RepositoryContractForStructureData
    fun makeRequestForElements()
}