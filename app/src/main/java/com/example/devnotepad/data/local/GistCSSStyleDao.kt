package com.example.devnotepad.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devnotepad.GistCSSStyle

/**
 * Dao для работы с таблицей стилей CSS, используемых для отображения gist'ов.
 * */
@Dao
interface GistCSSStyleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCSSStyle(gistCSSStyle: GistCSSStyle)

    @Query("DELETE FROM gist_css_style_table")
    suspend fun clearCSSStyleTable()

    @Query("SELECT * FROM gist_css_style_table LIMIT 1")
    fun getCSSStyle(): LiveData<List<GistCSSStyle>>

    @Query("SELECT * FROM gist_css_style_table LIMIT 1")
    suspend fun getCSSStyleSync(): GistCSSStyle
}