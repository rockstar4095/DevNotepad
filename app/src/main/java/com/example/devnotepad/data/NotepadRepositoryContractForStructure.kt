package com.example.devnotepad.data

import com.example.devnotepad.NotepadData

interface NotepadRepositoryContractForStructure : NotepadRepositoryContract {
    suspend fun getAllElementsSync(): List<NotepadData>
}