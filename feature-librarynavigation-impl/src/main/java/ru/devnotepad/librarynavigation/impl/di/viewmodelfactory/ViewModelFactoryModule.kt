package ru.devnotepad.librarynavigation.impl.di.viewmodelfactory

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {

    @Binds
    fun provideViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory
}