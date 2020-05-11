package com.example.devnotepad.di.modules

import com.example.devnotepad.ui.activity_main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [MainActivityViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}