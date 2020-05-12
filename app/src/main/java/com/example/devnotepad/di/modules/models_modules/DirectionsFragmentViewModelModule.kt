package com.example.devnotepad.di.modules.models_modules

import androidx.lifecycle.ViewModel
import com.example.devnotepad.di.ViewModelKey
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForCodeSnippets
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForHeaders
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForParagraphs
import com.example.devnotepad.ui.fragment_articles.ArticlesViewModel
import com.example.devnotepad.ui.fragment_directions.DirectionsViewModel
import com.example.devnotepad.ui.fragment_topics.TopicsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DirectionsFragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DirectionsViewModel::class)
    abstract fun bindDirectionsFragmentViewModel(directionsViewModel: DirectionsViewModel): ViewModel
}