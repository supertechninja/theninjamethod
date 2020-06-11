package com.mcwilliams.theninjamethod

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TheNinjaMethodApplication : Application() {

//    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//        return DaggerAppComponent.factory().create(this)
//    }

    override fun onCreate() {
        super.onCreate()

    }

    companion object {
        var isUserLoggedIn = false
    }
}