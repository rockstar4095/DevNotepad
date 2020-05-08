package com.example.devnotepad.data.rest

import com.example.devnotepad.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DevNotepadApi {

    @GET("devnotepad/api/get-directions.php")
    fun getDirections(): Call<List<DirectionOfStudy>>

    @GET("devnotepad/api/get-topics.php")
    fun getTopics(): Call<List<Topic>>

    @GET("devnotepad/api/get-articles.php")
    fun getArticles(): Call<List<Article>>

    /**
     * Запрашивает у сервера содержимое статьи, используя ее id как условие фильтра.
     * */
    @GET("devnotepad/api/get-article-headers.php")
    fun getArticleHeaders(@Query("articleId") articleId: Int): Call<List<ArticleHeader>>

    /**
     * Запрашивает у сервера содержимое статьи, используя ее id как условие фильтра.
     * */
    @GET("devnotepad/api/get-article-paragraphs.php")
    fun getArticleParagraphs(@Query("articleId") articleId: Int): Call<List<ArticleParagraph>>

    /** Test
    @GET("devnotepad/api/get-notepad-elements.php")
    fun getNotepadData(
        @Query("elementType") elementType: String,
        @Query("elementId") elementId: Int
    ): Call<List<NotepadData>>
    */
}