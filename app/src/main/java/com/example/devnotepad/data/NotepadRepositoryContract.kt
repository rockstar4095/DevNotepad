package com.example.devnotepad.data

import com.example.devnotepad.NotepadData

interface NotepadRepositoryContract {
    suspend fun insertElement(notepadData: NotepadData)
    suspend fun deleteElement(notepadData: NotepadData)
}