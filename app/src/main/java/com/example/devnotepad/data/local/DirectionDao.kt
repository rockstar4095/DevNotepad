package com.example.devnotepad.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.devnotepad.DirectionOfStudy

/**
 * DAO для работы с направлениями.
 * */
@Dao
interface DirectionDao {

    /**
     * Синхронное получение списка напрвлений используется для сравнения со списком, полученным с
     * сервера. Запрос к БД производится с помощью корутин.
     * */
    @Query("SELECT * FROM directions_table ORDER BY name ASC")
    suspend fun getAllDirectionsSync(): List<DirectionOfStudy>

    @Query("SELECT * FROM directions_table ORDER BY name ASC")
    fun getAllDirections(): LiveData<List<DirectionOfStudy>>

    /**
     * В случае, если направление уже существует, но есть необходимость заменить его содержимое
     * более новым, направление заменяется в БД.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirection(directionOfStudy: DirectionOfStudy)

    /**
     * В случае, если направление было удалено из БД сервера, оно будет удалено и в локальной БД.
     * */
    @Delete
    suspend fun deleteDirection(directionOfStudy: DirectionOfStudy)
}