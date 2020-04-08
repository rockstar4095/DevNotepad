package com.example.devnotepad.data.rest

import retrofit2.Call
import retrofit2.http.GET

interface DevNotepadApi {

    @GET("get-articles.php")
    fun getArticles(): Call<String>
}