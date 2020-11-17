package com.mcwilliams.theninjamethod.ui.settings

import com.mcwilliams.appinf.model.Athlete
import com.mcwilliams.settings.model.AthleteStats
import com.mcwilliams.settings.model.StravaAthlete

interface SettingsRepo {
    suspend fun authAthlete(code: String): Athlete?

    suspend fun fetchAthlete(): StravaAthlete?

    suspend fun fetchAthleteStats(id: String): AthleteStats?
}