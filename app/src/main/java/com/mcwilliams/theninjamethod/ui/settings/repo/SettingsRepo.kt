package com.mcwilliams.theninjamethod.ui.settings.repo

import com.mcwilliams.appinf.model.Athlete
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete

interface SettingsRepo {
    suspend fun authAthlete(code: String): Athlete?

    suspend fun fetchAthlete(): StravaAthlete?
}