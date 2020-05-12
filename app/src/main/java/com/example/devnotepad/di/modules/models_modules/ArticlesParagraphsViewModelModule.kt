package com.example.devnotepad.di.modules.models_modules

import androidx.lifecycle.ViewModel
import com.example.devnotepad.di.ViewModelKey
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForParagraphs
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ArticlesParagraphsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArticleContentViewModelForParagraphs::class)
    abstract fun bindArticlesParagraphsFragmentViewModel(articleContentViewModelForParagraphs: ArticleContentViewModelForParagraphs): ViewModel
}