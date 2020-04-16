package com.example.devnotepad.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.devnotepad.Topic

/**
 * DAO для работы с темами для изучения.
 * */
@Dao
interface TopicDao {

    /**
     * Синхронное получение списка тем используется для сравнения со списком, полученным с
     * сервера. Запрос к БД производится с помощью корутин.
     * */
    @Query("SELECT * FROM topics_table ORDER BY name ASC")
    suspend fun getAllTopicsSync(): List<Topic>

    @Query("SELECT * FROM topics_table ORDER BY name ASC")
    fun getAllTopics(): LiveData<List<Topic>>

    /**
     * В случае, если тема уже существует, но есть необходимость заменить её содержимое
     * более новым, тема заменяется в БД.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: Topic)

    /**
     * В случае, если тема была удалена из БД сервера, она будет удалена и в локальной БД.
     * */
    @Delete
    suspend fun deleteTopic(topic: Topic)
}