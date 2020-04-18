package com.example.devnotepad.data.rest

import com.example.devnotepad.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DevNotepadApi {

    @GET("get-directions.php")
    fun getDirections(): Call<List<DirectionOfStudy>>

    @GET("get-topics.php")
    fun getTopics(): Call<List<Topic>>

    @GET("get-articles.php")
    fun getArticles(): Call<List<Article>>

    /**
     * By article id.
     * */
    @GET("get-article-headers.php")
    fun getArticleHeaders(@Query("articleId") articleId: Int): Call<List<ArticleHeader>>

    /**
     * By article id.
     * */
    @GET("get-article-paragraphs.php")
    fun getArticleParagraphs(@Query("articleId") articleId: Int): Call<List<ArticleParagraph>>
}