package com.mcwilliams.theninjamethod.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper
@Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    private val ACCESS_TOKEN = "ACCESS_TOKEN"

    fun getAccessTokenFromPreference(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

}