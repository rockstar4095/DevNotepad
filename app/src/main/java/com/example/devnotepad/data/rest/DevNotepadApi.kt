package com.example.devnotepad.data.rest

import com.example.devnotepad.GistCSSStyle
import com.example.devnotepad.NotepadData
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

    @GET("devnotepad/api/get-gist-new-version-of-css-style.php")
    fun getGistNewVersionOfCSSStyle(): Call<List<GistCSSStyle>>

    @GET("devnotepad/api/get-gist-old-version-of-css-style.php")
    fun getGistOldVersionOfCSSStyle(): Call<List<GistCSSStyle>>
}