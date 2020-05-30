package com.mcwilliams.theninjamethod

import android.app.Application
import com.mcwilliams.theninjamethod.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TheNinjaMethodApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

    }

    companion object {
        var isUserLoggedIn = false
    }
}