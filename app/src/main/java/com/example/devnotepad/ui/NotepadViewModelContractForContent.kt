package com.example.devnotepad.ui

import com.example.devnotepad.data.NotepadRepositoryContractForContent

interface NotepadViewModelContractForContent : NotePadViewModelContract {
    val notepadRepository: NotepadRepositoryContractForContent
    fun makeRequestForElements(parentElementId: Int)
}