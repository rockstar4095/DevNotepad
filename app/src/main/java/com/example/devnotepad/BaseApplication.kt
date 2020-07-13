package com.example.devnotepad

import com.example.devnotepad.di.AppComponent
import com.example.devnotepad.di.DaggerAppComponent
import com.example.devnotepad.di.NewComponentHolder
import com.example.devnotepad.utils.CSSCodeSource
import com.example.devnotepad.utils.InternetConnectionChecker
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication() {

    companion object {
        lateinit var baseApplication: BaseApplication
        lateinit var appComponent: AppComponent
        lateinit var cssCodeSource: CSSCodeSource
    }

    override fun onCreate() {
        super.onCreate()
        baseApplication = this
        initializeAppComponent()
        registerNetworkStatusCallbacks()
        initializeCSSCodeSource()

        /**
         * new
         * */
        NewComponentHolder.initComponent(this)
    }

    private fun initializeCSSCodeSource() {
        cssCodeSource = CSSCodeSource(this)
    }

    private fun registerNetworkStatusCallbacks() {
        InternetConnectionChecker.registerNetworkCallbacks()
    }

    private fun initializeAppComponent() {
        appComponent = DaggerAppComponent.builder().application(this).build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}