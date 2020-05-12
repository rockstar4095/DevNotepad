package com.example.devnotepad.di.modules

import com.example.devnotepad.di.modules.models_modules.*
import com.example.devnotepad.ui.fragment_article_content.ArticleContentFragment
import com.example.devnotepad.ui.fragment_articles.ArticlesFragment
import com.example.devnotepad.ui.fragment_directions.DirectionsFragment
import com.example.devnotepad.ui.fragment_topics.TopicsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsBuildersModule {

    @ContributesAndroidInjector(
        modules = [DirectionsFragmentViewModelModule::class]
    )
    abstract fun contributeDirectionsFragment(): DirectionsFragment

    @ContributesAndroidInjector(
        modules = [TopicsFragmentViewModelModule::class]
    )
    abstract fun contributeTopicsFragment(): TopicsFragment

    @ContributesAndroidInjector(
        modules = [ArticlesFragmentViewModelModule::class]
    )
    abstract fun contributeArticlesFragment(): ArticlesFragment

    @ContributesAndroidInjector(
        modules = [
            ArticlesHeadersViewModelModule::class,
            ArticlesParagraphsViewModelModule::class,
            ArticlesCodeSnippetsViewModelModule::class
        ]
    )
    abstract fun contributeArticlesContentFragment(): ArticleContentFragment
}