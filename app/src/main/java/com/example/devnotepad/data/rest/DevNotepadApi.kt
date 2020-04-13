package com.example.devnotepad.data.rest

import com.example.devnotepad.DirectionOfStudy
import retrofit2.Call
import retrofit2.http.GET

interface DevNotepadApi {

    @GET("get-articles.php")
    fun getDirections(): Call<List<DirectionOfStudy>>
}