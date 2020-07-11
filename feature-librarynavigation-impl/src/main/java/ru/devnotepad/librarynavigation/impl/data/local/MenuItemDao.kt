package ru.devnotepad.librarynavigation.impl.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem

@Dao
interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(nestedMenuItem: NestedMenuItem)

    @Query("SELECT * FROM menu_items WHERE parentId = :parentId")
    fun getMenuItems(parentId: Int): LiveData<List<NestedMenuItem>>
}