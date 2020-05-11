package com.example.devnotepad.ui

import com.example.devnotepad.data.repositories.RepositoryContractForArticlesContent

interface NotepadViewModelContractForContent : NotePadViewModelContract {
    val repositoryForArticlesContent: RepositoryContractForArticlesContent
    fun makeRequestForElements(parentElementId: Int)
}