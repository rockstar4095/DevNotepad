package ru.devnotepad.librarynavigation.impl.ui

import androidx.lifecycle.ViewModel
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem

class LibraryNavigationViewModel : ViewModel() {
    fun getFakeMenuItems(): List<NestedMenuItem> =
        listOf(
            NestedMenuItem(
                1,
                false,
                0,
                true,
                listOf(),
                "menu item 1",
                1000
            ),

            NestedMenuItem(
                2,
                false,
                0,
                true,
                listOf(),
                "menu item 2",
                1000
            ),

            NestedMenuItem(
                3,
                false,
                0,
                true,
                listOf(),
                "menu item 3",
                1000
            )
        )
}