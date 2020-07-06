package ru.devnotepad.librarynavigation.impl.data

import android.content.Context
import androidx.lifecycle.LiveData
import ru.devnotepad.librarynavigation.impl.data.local.MenuItemDatabase
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem

// temp context
class MenuItemsRepository(context: Context) {

    private val menuItemsDao = MenuItemDatabase.getDatabase(context).menuItemDao()

    fun getMenuItems(parentId: Int): LiveData<List<NestedMenuItem>> =
        menuItemsDao.getMenuItems(parentId)

    suspend fun insertMenuItem(nestedMenuItem: NestedMenuItem) =
        menuItemsDao.insertMenuItem(nestedMenuItem)
}