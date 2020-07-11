package ru.devnotepad.librarynavigation.impl.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.devnotepad.librarynavigation.impl.data.MenuItemsRepository
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem
import javax.inject.Inject

class LibraryNavigationViewModel @Inject constructor(
    private val menuItemsRepository: MenuItemsRepository
) : ViewModel() {

    private val userMenuPathIds = ArrayList<Int>()
    var immersionLevel = 0

    fun getMenuItems(parentId: Int): LiveData<List<NestedMenuItem>> =
        menuItemsRepository.getMenuItems(parentId)

    fun recordUserPathIds(menuItemId: Int) { userMenuPathIds.add(menuItemId) }

    fun increaseImmersionLevel() { immersionLevel++ }
    fun decreaseImmersionLevel() { immersionLevel-- }

    fun getPreviousPathPoint(): Int {
        if (userMenuPathIds.size == 0) return 0
        if (userMenuPathIds.size == 1) userMenuPathIds.clear().also { return 0 }
        userMenuPathIds.let { it.removeAt(it.lastIndex) }.also { return userMenuPathIds.last() }
    }
}