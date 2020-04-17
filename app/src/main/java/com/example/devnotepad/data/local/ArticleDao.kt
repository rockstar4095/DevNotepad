package com.example.devnotepad.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.devnotepad.Article

/**
 * DAO для работы со статьями.
 * */
@Dao
interface ArticleDao {

    /**
     * Синхронное получение списка статей используется для сравнения со списком, полученным с
     * сервера. Запрос к БД производится с помощью корутин.
     * */
    @Query("SELECT * FROM articles_table ORDER BY name ASC")
    suspend fun getAllArticlesSync(): List<Article>

    @Query("SELECT * FROM articles_table ORDER BY name ASC")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles_table WHERE name = :articleName")
    fun getArticle(articleName: String): LiveData<Article>

    /**
     * В случае, если статья уже существует, но есть необходимость заменить её содержимое
     * более новым, статья заменяется в БД.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    /**
     * В случае, если статья была удалена из БД сервера, она будет удалена и в локальной БД.
     * */
    @Delete
    suspend fun deleteArticle(article: Article)
}