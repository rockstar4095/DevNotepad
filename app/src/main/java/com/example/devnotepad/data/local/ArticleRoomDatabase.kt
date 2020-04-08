package com.example.devnotepad.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.devnotepad.Article

@Database(entities = [Article::class], version = 1)
public abstract class ArticleRoomDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: ArticleRoomDatabase? = null

        fun getDatabase(context: Context): ArticleRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleRoomDatabase::class.java,
                    "article_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}