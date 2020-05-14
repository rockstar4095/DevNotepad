package com.example.devnotepad.di

import android.app.Application
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.di.modules.ActivityBuildersModule
import com.example.devnotepad.di.modules.FragmentsBuildersModule
import com.example.devnotepad.di.modules.RetrofitModule
import com.example.devnotepad.di.modules.ViewModelFactoryModule
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForCodeSnippets
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForHeaders
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForImages
import com.example.devnotepad.ui.fragment_article_content.ArticleContentViewModelForParagraphs
import com.example.devnotepad.ui.fragment_articles.ArticlesViewModel
import com.example.devnotepad.ui.fragment_directions.DirectionsViewModel
import com.example.devnotepad.ui.fragment_topics.TopicsViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        FragmentsBuildersModule::class,
        ViewModelFactoryModule::class,
        RetrofitModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(directionsViewModel: DirectionsViewModel)
    fun inject(topicsViewModel: TopicsViewModel)
    fun inject(articlesViewModel: ArticlesViewModel)
    fun inject(articleContentViewModelForHeaders: ArticleContentViewModelForHeaders)
    fun inject(articleContentViewModelForParagraphs: ArticleContentViewModelForParagraphs)
    fun inject(articleContentViewModelForCodeSnippets: ArticleContentViewModelForCodeSnippets)
    fun inject(articleContentViewModelForImages: ArticleContentViewModelForImages)
}