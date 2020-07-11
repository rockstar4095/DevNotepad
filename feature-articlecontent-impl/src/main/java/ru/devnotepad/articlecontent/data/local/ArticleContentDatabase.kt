package ru.devnotepad.articlecontent.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.devnotepad.articlecontent.entities.ArticleCodeSnippet
import ru.devnotepad.articlecontent.entities.ArticleHeader
import ru.devnotepad.articlecontent.entities.ArticleImage
import ru.devnotepad.articlecontent.entities.ArticleParagraph

@Database(
    entities = [
        ArticleHeader::class,
        ArticleParagraph::class,
        ArticleCodeSnippet::class,
        ArticleImage::class
    ], version = 1
)
abstract class ArticleContentDatabase : RoomDatabase() {

    abstract fun articleHeaderDao(): ArticleHeaderDao
    abstract fun articleParagraphDao(): ArticleParagraphDao
    abstract fun articleCodeSnippetDao(): ArticleCodeSnippetDao
    abstract fun articleImageDao(): ArticleImageDao

    companion object {

        // Singleton prevents multiple instances of database opening at the same time.
        // TODO: replace with dagger injection.
        @Volatile
        private var INSTANCE: ArticleContentDatabase? = null

        fun getDatabase(context: Context): ArticleContentDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleContentDatabase::class.java,
                    "article_database"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}