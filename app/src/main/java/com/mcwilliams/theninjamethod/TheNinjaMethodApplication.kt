package com.mcwilliams.theninjamethod

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TheNinjaMethodApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }

    companion object {
        var isUserLoggedIn = false
    }
}