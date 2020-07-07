package ru.devnotepad.librarynavigation.impl.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.devnotepad.librarynavigation.impl.di.viewmodelfactory.ViewModelKey
import ru.devnotepad.librarynavigation.impl.ui.LibraryNavigationViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LibraryNavigationViewModel::class)
    fun provideLibraryNavigationViewModel(
        libraryNavigationViewModel: LibraryNavigationViewModel
    ): ViewModel
}