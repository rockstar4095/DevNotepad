package ru.devnotepad.librarynavigation.impl.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.devnotepad.librarynavigation.impl.data.MenuItemsRepository
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem
import javax.inject.Inject

class LibraryNavigationViewModel @Inject constructor(
    private val menuItemsRepository: MenuItemsRepository
) : ViewModel() {
    fun getMenuItems(parentId: Int): LiveData<List<NestedMenuItem>> =
        menuItemsRepository.getMenuItems(parentId)
}