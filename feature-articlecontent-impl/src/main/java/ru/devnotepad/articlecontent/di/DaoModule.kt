package ru.devnotepad.articlecontent.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.devnotepad.articlecontent.data.local.*

@Module
class DaoModule(private val context: Context) {

    @Provides
    fun provideArticleHeaderDao(): ArticleHeaderDao =
        ArticleContentDatabase.getDatabase(context).articleHeaderDao()

    @Provides
    fun provideArticleParagraphDao(): ArticleParagraphDao =
        ArticleContentDatabase.getDatabase(context).articleParagraphDao()

    @Provides
    fun provideArticleCodeSnippetDao(): ArticleCodeSnippetDao =
        ArticleContentDatabase.getDatabase(context).articleCodeSnippetDao()

    @Provides
    fun provideArticleImageDao(): ArticleImageDao =
        ArticleContentDatabase.getDatabase(context).articleImageDao()
}