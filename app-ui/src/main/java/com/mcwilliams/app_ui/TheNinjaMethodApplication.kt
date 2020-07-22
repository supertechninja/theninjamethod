package com.mcwilliams.app_ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TheNinjaMethodApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}