package com.example.devnotepad.di.modules.models_modules

import androidx.lifecycle.ViewModel
import com.example.devnotepad.di.ViewModelKey
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForImages
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ArticlesImagesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArticleContentViewModelForImages::class)
    abstract fun bindArticlesImagesFragmentViewModel(articleContentViewModelForImages: ArticleContentViewModelForImages): ViewModel
}