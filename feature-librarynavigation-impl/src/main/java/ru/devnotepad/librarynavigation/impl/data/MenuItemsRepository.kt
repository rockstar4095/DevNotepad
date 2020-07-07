package ru.devnotepad.librarynavigation.impl.data

import androidx.lifecycle.LiveData
import ru.devnotepad.librarynavigation.impl.data.local.MenuItemDao
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem
import javax.inject.Inject


class MenuItemsRepository @Inject constructor(
    private val menuItemDao: MenuItemDao
) {

    fun getMenuItems(parentId: Int): LiveData<List<NestedMenuItem>> =
        menuItemDao.getMenuItems(parentId)

    private suspend fun insertMenuItem(nestedMenuItem: NestedMenuItem) =
        menuItemDao.insertMenuItem(nestedMenuItem)
}

//val menuItems = listOf(
//    NestedMenuItem(
//        1,
//        0,
//        0,
//        "menu item 1",
//        1000
//    ),
//
//    NestedMenuItem(
//        2,
//        0,
//        0,
//        "menu item 2",
//        1000
//    ),
//
//    NestedMenuItem(
//        3,
//        0,
//        0,
//        "menu item 3",
//        1000
//    ),
//
//    NestedMenuItem(
//        4,
//        1,
//        0,
//        "menu item 4",
//        1000
//    ),
//
//    NestedMenuItem(
//        5,
//        1,
//        0,
//        "menu item 5",
//        1000
//    ),
//
//    NestedMenuItem(
//        6,
//        4,
//        0,
//        "menu item 6",
//        1000
//    ),
//
//    NestedMenuItem(
//        7,
//        6,
//        0,
//        "menu item 7",
//        1000
//    )
//)