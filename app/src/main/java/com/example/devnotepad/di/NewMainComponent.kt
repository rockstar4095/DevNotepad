package com.example.devnotepad.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.devnotepad.common.ComponentDependencies

@Component(
    modules = [
        NewComponentDependenciesModule::class,
        NewInteractorsModule::class
    ]
)
interface NewMainComponent : ComponentDependencies {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): NewMainComponent
    }
}