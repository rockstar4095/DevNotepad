package com.example.devnotepad.di.modules.models_modules

import androidx.lifecycle.ViewModel
import com.example.devnotepad.di.ViewModelKey
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForHeaders
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ArticlesHeadersViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArticleContentViewModelForHeaders::class)
    abstract fun bindArticlesHeadersFragmentViewModel(articleContentViewModelForHeaders: ArticleContentViewModelForHeaders): ViewModel
}