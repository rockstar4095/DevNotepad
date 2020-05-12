package com.example.devnotepad.di

import android.app.Application
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.di.modules.ActivityBuildersModule
import com.example.devnotepad.di.modules.FragmentsBuildersModule
import com.example.devnotepad.di.modules.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        FragmentsBuildersModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}