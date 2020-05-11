package com.example.devnotepad.data.repositories

import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.repositories.NotepadRepositoryContract

/**
 * Общий интерфейс репозиториев, которые работают со структурными данными.
 * */
interface RepositoryContractForStructureData :
    NotepadRepositoryContract {
    suspend fun getAllElementsSync(): List<NotepadData>
}