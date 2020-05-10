package com.example.devnotepad.data

import com.example.devnotepad.NotepadData

interface NotepadRepositoryContractForContent : NotepadRepositoryContract {
    suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData>
}