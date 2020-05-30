package com.mcwilliams.theninjamethod.ui.settings.repo

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.authorize.model.LoginResult
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete
import com.mcwilliams.theninjamethod.ui.settings.data.TokenResponse
import com.mcwilliams.theninjamethod.utils.IRepository

interface SettingsRepo {
        suspend fun authAthlete(code:String): Result<Athlete>
}