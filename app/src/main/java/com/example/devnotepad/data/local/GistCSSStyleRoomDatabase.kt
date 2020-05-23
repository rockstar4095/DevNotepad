package com.example.devnotepad.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.devnotepad.GistCSSStyle

@Database(entities = [GistCSSStyle::class], version = 2)
abstract class GistCSSStyleRoomDatabase : RoomDatabase() {

    abstract fun gistCSSStyleDao(): GistCSSStyleDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: GistCSSStyleRoomDatabase? = null

        fun getDatabase(context: Context): GistCSSStyleRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GistCSSStyleRoomDatabase::class.java,
                    "gist_css_style_database"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}