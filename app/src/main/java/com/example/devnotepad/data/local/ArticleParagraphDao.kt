package com.example.devnotepad.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.ArticleParagraph

/**
 * DAO для работы с содержимым статей.
 * */
@Dao
interface ArticleParagraphDao {

    @Query("SELECT * FROM articles_headers_table")
    fun getArticleHeaders(): LiveData<List<ArticleHeader>>

    /**
     * Синхронное получение списка используется для сравнения со списком, полученным с
     * сервера. Запрос к БД производится с помощью корутин.
     * */
    @Query("SELECT * FROM articles_headers_table WHERE articleIdFromServer = :articleIdFromServer")
    suspend fun getArticleHeadersSync(articleIdFromServer: Int): List<ArticleHeader>

    /**
     * В случае, если данные уже существуют, но есть необходимость заменить содержимое
     * более новым, данные заменяются в БД.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleHeader(articleHeader: ArticleHeader)

    /**
     * В случае, если данные были удалены из БД сервера, они будут удалены и в локальной БД.
     * */
    @Delete
    suspend fun deleteArticleHeader(articleHeader: ArticleHeader)


    @Query("SELECT * FROM articles_paragraphs_table")
    fun getArticleParagraphs(): LiveData<List<ArticleParagraph>>

    /**
     * Синхронное получение списка используется для сравнения со списком, полученным с
     * сервера. Запрос к БД производится с помощью корутин.
     * */
    @Query("SELECT * FROM articles_paragraphs_table WHERE articleIdFromServer = :articleIdFromServer")
    suspend fun getArticleParagraphsSync(articleIdFromServer: Int): List<ArticleParagraph>

    /**
     * В случае, если данные уже существуют, но есть необходимость заменить содержимое
     * более новым, данные заменяются в БД.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleParagraph(articleParagraph: ArticleParagraph)

    /**
     * В случае, если данные были удалены из БД сервера, они будут удалены и в локальной БД.
     * */
    @Delete
    suspend fun deleteArticleParagraph(articleParagraph: ArticleParagraph)
}