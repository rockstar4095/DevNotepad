package com.example.devnotepad.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.devnotepad.*

@Database(
    entities = [
        DirectionOfStudy::class,
        Topic::class,
        Article::class,
        ArticleHeader::class,
        ArticleParagraph::class
    ], version = 3
)
abstract class KnowledgeRoomDatabase : RoomDatabase() {

    abstract fun directionDao(): DirectionDao
    abstract fun topicDao(): TopicDao
    abstract fun articleDao(): ArticleDao
    abstract fun articleParagraphDao(): ArticleParagraphDao
    abstract fun articleHeaderDao(): ArticleHeaderDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: KnowledgeRoomDatabase? = null

        fun getDatabase(context: Context): KnowledgeRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KnowledgeRoomDatabase::class.java,
                    "article_database"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}