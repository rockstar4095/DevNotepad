package com.example.devnotepad.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.devnotepad.Article
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.Topic

@Database(entities = [Article::class, Topic::class, DirectionOfStudy::class], version = 3)
public abstract class ArticleRoomDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun topicDao(): TopicDao
    abstract fun directionDao(): DirectionDao

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
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}