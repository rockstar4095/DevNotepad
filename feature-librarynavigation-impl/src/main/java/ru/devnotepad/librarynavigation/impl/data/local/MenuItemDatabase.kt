package ru.devnotepad.librarynavigation.impl.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem

@Database(
    entities = [
        NestedMenuItem::class
    ], version = 1
)
abstract class MenuItemDatabase : RoomDatabase() {

    abstract fun menuItemDao(): MenuItemDao

    companion object {
        @Volatile
        private var INSTANCE: MenuItemDatabase? = null
        private const val DATABASE_NAME = "menu_item_database"

        fun getDatabase(context: Context): MenuItemDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MenuItemDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                    .also { INSTANCE = it }
            }
        }
    }
}