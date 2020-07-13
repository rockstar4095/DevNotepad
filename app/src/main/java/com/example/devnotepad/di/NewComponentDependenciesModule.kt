package com.example.devnotepad.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.devnotepad.articlecontent.di.ArticleContentDependencies
import ru.devnotepad.common.ComponentDependencies
import ru.devnotepad.common.ComponentDependenciesKey
import ru.devnotepad.librarynavigation.impl.di.LibraryNavigationDependencies

@Module
interface NewComponentDependenciesModule {

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ArticleContentDependencies::class)
    fun provideArticleContentDependencies(component: NewMainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(LibraryNavigationDependencies::class)
    fun provideLibraryNavigationDependencies(component: NewMainComponent): ComponentDependencies
}