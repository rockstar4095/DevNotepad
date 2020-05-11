package com.example.devnotepad.ui

import com.example.devnotepad.data.repositories.RepositoryContractForStructureData

interface NotepadViewModelContractForStructure : NotePadViewModelContract {
    val repositoryForStructureData: RepositoryContractForStructureData
    fun makeRequestForElements()
}