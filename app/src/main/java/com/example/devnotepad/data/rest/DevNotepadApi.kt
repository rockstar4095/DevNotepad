package com.example.devnotepad.data.rest

import com.example.devnotepad.Article
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.Topic
import retrofit2.Call
import retrofit2.http.GET

interface DevNotepadApi {

    @GET("get-directions.php")
    fun getDirections(): Call<List<DirectionOfStudy>>

    @GET("get-topics.php")
    fun getTopics(): Call<List<Topic>>

    @GET("get-articles.php")
    fun getArticles(): Call<List<Article>>
}