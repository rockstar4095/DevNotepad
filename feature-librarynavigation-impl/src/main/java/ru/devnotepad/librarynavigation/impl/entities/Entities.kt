package ru.devnotepad.librarynavigation.impl.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class NestedMenuItem(
    @PrimaryKey
    val idFromServer: Int,
    val parentId: Int,
    val parentIdFromServer: Int,
    val name: String,
    val timeWhenDataChanged: Long
)