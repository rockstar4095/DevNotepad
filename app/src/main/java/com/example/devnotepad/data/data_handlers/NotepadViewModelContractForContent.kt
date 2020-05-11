package com.example.devnotepad.data.data_handlers

import com.example.devnotepad.data.data_handlers.NotePadViewModelContract
import com.example.devnotepad.data.repositories.RepositoryContractForArticlesContent

interface NotepadViewModelContractForContent :
    NotePadViewModelContract {
    val repositoryForArticlesContent: RepositoryContractForArticlesContent
    fun makeRequestForElements(parentElementId: Int)
}