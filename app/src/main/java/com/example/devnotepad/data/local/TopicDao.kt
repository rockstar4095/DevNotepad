package com.example.devnotepad.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devnotepad.Topic

@Dao
interface TopicDao {

    @Query("SELECT * FROM topics_table ORDER BY name ASC")
    fun getAllTopics(): LiveData<List<Topic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topic: Topic)
}