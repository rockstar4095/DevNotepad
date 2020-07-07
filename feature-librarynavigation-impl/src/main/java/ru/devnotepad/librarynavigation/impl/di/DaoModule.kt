package ru.devnotepad.librarynavigation.impl.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.devnotepad.librarynavigation.impl.data.local.MenuItemDao
import ru.devnotepad.librarynavigation.impl.data.local.MenuItemDatabase

@Module
class DaoModule(private val context: Context) {

    @Provides
    fun provideMenuItemsDao(): MenuItemDao =
        MenuItemDatabase.getDatabase(context).menuItemDao()
}