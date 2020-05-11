package com.example.devnotepad.data.repositories

import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.repositories.NotepadRepositoryContract

/**
 * Общий интерфейс репозиториев, которые работают с содержимым статей.
 * */
interface RepositoryContractForArticlesContent : NotepadRepositoryContract {
    suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData>
}