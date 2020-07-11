package ru.devnotepad.librarynavigation.impl.di

import ru.devnotepad.librarynavigation.impl.ui.LibraryNavigationFragment

object ComponentHolder {

    var component: LibraryNavigationComponent? = null
        private set

    fun initComponent(fragment: LibraryNavigationFragment) {
        component = DaggerLibraryNavigationComponent.builder()
            .daoModule(DaoModule(fragment.requireContext()))
            .build().apply {
                inject(fragment)
            }
    }

    fun clearComponent() = let { component = null }
}