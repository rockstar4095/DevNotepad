package com.example.devnotepad.ui

import com.example.devnotepad.data.NotepadRepositoryContractForStructure

interface NotepadViewModelContractForStructure : NotePadViewModelContract {
    val notepadRepository: NotepadRepositoryContractForStructure
    fun makeRequestForElements()
}