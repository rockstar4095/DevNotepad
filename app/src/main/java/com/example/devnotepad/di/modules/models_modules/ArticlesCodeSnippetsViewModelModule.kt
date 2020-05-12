package com.example.devnotepad.di.modules.models_modules

import androidx.lifecycle.ViewModel
import com.example.devnotepad.di.ViewModelKey
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForCodeSnippets
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ArticlesCodeSnippetsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArticleContentViewModelForCodeSnippets::class)
    abstract fun bindArticlesCodeSnippetsFragmentViewModel(articleContentViewModelForCodeSnippets: ArticleContentViewModelForCodeSnippets): ViewModel
}