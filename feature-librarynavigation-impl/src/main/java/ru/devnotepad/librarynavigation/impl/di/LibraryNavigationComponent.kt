package ru.devnotepad.librarynavigation.impl.di

import dagger.Component
import ru.devnotepad.librarynavigation.impl.data.MenuItemsRepository
import ru.devnotepad.librarynavigation.impl.di.viewmodelfactory.ViewModelFactoryModule
import ru.devnotepad.librarynavigation.impl.ui.LibraryNavigationFragment

@Component(
    modules = [
        DaoModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class
    ]
)
interface LibraryNavigationComponent {
    fun inject(menuItemsRepository: MenuItemsRepository)
    fun inject(libraryNavigationFragment: LibraryNavigationFragment)
}