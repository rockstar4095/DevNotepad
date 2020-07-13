package com.example.devnotepad.di

import android.app.Application

object NewComponentHolder {

    lateinit var mainComponent: NewMainComponent

    fun initComponent(application: Application) {
        mainComponent = DaggerNewMainComponent.builder()
            .context(application)
            .build()
    }
}