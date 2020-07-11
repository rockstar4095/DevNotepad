package ru.devnotepad.articlecontent.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.devnotepad.articlecontent.entities.ArticleCodeSnippet

@Dao
interface ArticleCodeSnippetDao {

    @Query("SELECT * FROM articles_code_snippets_table WHERE articleIdFromServer = :articleId")
    fun getArticleCodeSnippets(articleId: Int): LiveData<List<ArticleCodeSnippet>>

    /**
     * Синхронное получение списка используется для сравнения со списком, полученным с
     * сервера. Запрос к БД производится с помощью корутин.
     * */
    @Query("SELECT * FROM articles_code_snippets_table WHERE articleIdFromServer = :articleIdFromServer")
    suspend fun getArticleCodeSnippetsSync(articleIdFromServer: Int): List<ArticleCodeSnippet>

    /**
     * В случае, если данные уже существуют, но есть необходимость заменить содержимое
     * более новым, данные заменяются в БД.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleCodeSnippet(articleCodeSnippet: ArticleCodeSnippet)

    /**
     * В случае, если данные были удалены из БД сервера, они будут удалены и в локальной БД.
     * */
    @Delete
    suspend fun deleteArticleCodeSnippet(articleCodeSnippet: ArticleCodeSnippet)

    /**TODO: REFACTOR, RENAME*/

    /**
     * Обновляет поле webViewHeight.
     * */
    @Query("UPDATE articles_code_snippets_table SET viewHeight = :webViewHeight WHERE idFromServer = :articleCodeSnippetId")
    suspend fun updateWebViewHeight(webViewHeight: Int, articleCodeSnippetId: Int)

    /**
     * Получает значение webViewHeight.
     * */
    @Query("SELECT viewHeight FROM articles_code_snippets_table WHERE idFromServer = :articleCodeSnippetId")
    suspend fun getWebViewHeight(articleCodeSnippetId: Int): Int
}