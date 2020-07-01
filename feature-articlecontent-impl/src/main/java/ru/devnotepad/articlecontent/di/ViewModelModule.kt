package ru.devnotepad.articlecontent.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.devnotepad.articlecontent.di.viewmodelfactory.ViewModelKey
import ru.devnotepad.articlecontent.ui.ArticleContentViewModel

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ArticleContentViewModel::class)
    fun provideArticleContentViewModel(
        articleContentViewModel: ArticleContentViewModel
    ): ViewModel
}