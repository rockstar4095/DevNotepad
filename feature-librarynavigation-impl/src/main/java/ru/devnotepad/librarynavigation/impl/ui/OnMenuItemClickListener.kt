package ru.devnotepad.librarynavigation.impl.ui

import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem

interface OnMenuItemClickListener {
    fun onMenuItemClick(nestedMenuItem: NestedMenuItem)
}