package com.mcwilliams.theninjamethod

import android.app.Application

class TheNinjaMethodApplication : Application() {

    override fun onCreate() {
        super.onCreate()



    }

    companion object {
        var isUserLoggedIn = false
    }
}