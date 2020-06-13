package ru.devnotepad.articlecontent.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.devnotepad.articlecontent.entities.ArticleHeader

@Dao
interface ArticleHeaderDao {

    // TODO: try to make request using article id.
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
}