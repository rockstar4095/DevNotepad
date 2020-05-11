package com.example.devnotepad.data.rest

import com.example.devnotepad.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DevNotepadApi {
    @GET("devnotepad/api/get-notepad-elements.php")
    fun getContentData(
        @Query("elementType") elementType: String,
        @Query("parentElementId") parentElementId: Int
    ): Call<List<NotepadData>>

    @GET("devnotepad/api/get-notepad-elements.php")
    fun getStructureData(
        @Query("elementType") elementType: String
    ): Call<List<NotepadData>>
}