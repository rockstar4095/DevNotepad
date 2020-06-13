package ru.devnotepad.articlecontent.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.devnotepad.articlecontent.entities.ArticleImage

@Dao
interface ArticleImageDao {

    @Query("SELECT * FROM articles_images_table")
    fun getArticleImages(): LiveData<List<ArticleImage>>

    /**
     * Синхронное получение списка используется для сравнения со списком, полученным с
     * сервера. Запрос к БД производится с помощью корутин.
     * */
    @Query("SELECT * FROM articles_images_table WHERE articleIdFromServer = :articleIdFromServer")
    suspend fun getArticleImagesSync(articleIdFromServer: Int): List<ArticleImage>

    /**
     * В случае, если данные уже существуют, но есть необходимость заменить содержимое
     * более новым, данные заменяются в БД.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleImage(articleImage: ArticleImage)

    /**
     * В случае, если данные были удалены из БД сервера, они будут удалены и в локальной БД.
     * */
    @Delete
    suspend fun deleteArticleImage(articleImage: ArticleImage)

    /**
     * Обновляет поле webViewHeight.
     * */
    @Query("UPDATE articles_images_table SET viewHeight = :webViewHeight WHERE idFromServer = :imageId")
    suspend fun updateWebViewHeight(webViewHeight: Int, imageId: Int)

    /**
     * Получает значение webViewHeight.
     * */
    @Query("SELECT viewHeight FROM articles_images_table WHERE idFromServer = :imageId")
    suspend fun getWebViewHeight(imageId: Int): Int
}