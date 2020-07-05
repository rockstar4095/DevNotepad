package ru.devnotepad.librarynavigation.impl.entities

data class NestedMenuItem(
    private val idFromServer: Int,
    private val hasParent: Boolean,
    private val parentIdFromServer: Int,
    private val hasChildren: Boolean,
    private val childrenList: List<NestedMenuItem>,
    val name: String,
    private val timeWhenDataChanged: Long
)