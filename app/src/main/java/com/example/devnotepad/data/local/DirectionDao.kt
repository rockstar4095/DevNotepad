package com.example.devnotepad.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.devnotepad.DirectionOfStudy

@Dao
interface DirectionDao {

    @Query("SELECT * FROM directions_table ORDER BY name ASC")
    suspend fun getAllDirectionsSync(): List<DirectionOfStudy>

    @Query("SELECT * FROM directions_table ORDER BY name ASC")
    fun getAllDirections(): LiveData<List<DirectionOfStudy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(directionOfStudy: DirectionOfStudy)

    @Delete
    suspend fun delete(directionOfStudy: DirectionOfStudy)
}