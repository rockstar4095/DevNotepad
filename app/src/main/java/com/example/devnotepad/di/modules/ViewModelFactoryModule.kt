package com.example.devnotepad.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.devnotepad.ui.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelProviderFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}