package com.example.devnotepad.di

import android.app.Application
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.data.repositories.*
import com.example.devnotepad.di.modules.ActivityBuildersModule
import com.example.devnotepad.di.modules.FragmentsBuildersModule
import com.example.devnotepad.di.modules.RetrofitModule
import com.example.devnotepad.di.modules.ViewModelFactoryModule
import com.example.devnotepad.utils.CSSCodeSource
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

    fun inject(directionsRepository: DirectionsRepository)
    fun inject(topicsRepository: TopicsRepository)
    fun inject(articlesRepository: ArticlesRepository)
    fun inject(articlesHeadersRepository: ArticlesHeadersRepository)
    fun inject(articlesParagraphsRepository: ArticlesParagraphsRepository)
    fun inject(articlesCodeSnippetsRepository: ArticlesCodeSnippetsRepository)
    fun inject(articlesImagesRepository: ArticlesImagesRepository)

    fun inject(cssCodeSource: CSSCodeSource)
}