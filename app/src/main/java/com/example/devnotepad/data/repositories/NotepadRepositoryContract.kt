package com.example.devnotepad.data.repositories

import com.example.devnotepad.NotepadData

/**
 * Общий интерфейс репозиториев.
 * */
interface NotepadRepositoryContract {
    suspend fun insertElement(notepadData: NotepadData)
    suspend fun deleteElement(notepadData: NotepadData)
}