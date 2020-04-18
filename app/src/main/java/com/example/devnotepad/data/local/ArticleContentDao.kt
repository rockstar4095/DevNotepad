package com.example.devnotepad.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.ArticleParagraph

@Dao
interface ArticleContentDao {

    @Query("SELECT * FROM articles_headers_table WHERE articleIdFromServer = :articleIdFromServer")
    fun getArticleHeaders(articleIdFromServer: Int): LiveData<List<ArticleHeader>>

    @Query("SELECT * FROM articles_headers_table WHERE articleIdFromServer = :articleIdFromServer")
    suspend fun getArticleHeadersSync(articleIdFromServer: Int): List<ArticleHeader>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleHeader(articleHeader: ArticleHeader)

    @Delete
    suspend fun deleteArticleHeader(articleHeader: ArticleHeader)


    @Query("SELECT * FROM articles_paragraphs_table WHERE articleIdFromServer = :articleIdFromServer")
    fun getArticleParagraphs(articleIdFromServer: Int): LiveData<List<ArticleParagraph>>

    @Query("SELECT * FROM articles_paragraphs_table WHERE articleIdFromServer = :articleIdFromServer")
    suspend fun getArticleParagraphsSync(articleIdFromServer: Int): List<ArticleParagraph>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleParagraph(articleParagraph: ArticleParagraph)

    @Delete
    suspend fun deleteArticleParagraph(articleParagraph: ArticleParagraph)
}