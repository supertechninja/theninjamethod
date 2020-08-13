package com.mcwilliams.settings.repo

import com.mcwilliams.appinf.model.Athlete
import com.mcwilliams.settings.StravaAthlete

interface SettingsRepo {
    suspend fun authAthlete(code: String): Athlete?

    suspend fun fetchAthlete(): StravaAthlete?
}