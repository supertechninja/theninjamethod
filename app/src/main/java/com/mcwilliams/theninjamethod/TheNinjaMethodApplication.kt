package com.mcwilliams.theninjamethod

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mcwilliams.data.workoutdb.Workout
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class TheNinjaMethodApplication() : Application() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        preferences = this.getSharedPreferences(
            this.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        if (!preferences.getBoolean("hasRetrievedRoutines", false)) {
            GlobalScope.launch {
                setupRoutines()
            }
            preferences.edit().putBoolean("hasRetrievedRoutines", true).apply()
        }

    }

    companion object {
        var isUserLoggedIn = false
    }

    private fun setupRoutines() {
        val jsonfile: String =
            this.assets.open("routines.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val workout = gson.fromJson(jsonfile, Workout::class.java)
//        routinesRepository.addRoutine(workout)

    }
}