package ru.devnotepad.articlecontent.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.devnotepad.articlecontent.entities.ArticleParagraph

@Dao
interface ArticleParagraphDao {

    @Query("SELECT * FROM articles_paragraphs_table WHERE articleIdFromServer = :articleId")
    fun getArticleParagraphs(articleId: Int): LiveData<List<ArticleParagraph>>

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