package com.mcwilliams.theninjamethod.ui.settings.repo

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.ActivityStats
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.StravaAthlete
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete

interface SettingsRepo {
    suspend fun authAthlete(code: String): Result<Athlete?>

    suspend fun fetchAthlete(): Result<StravaAthlete?>

    suspend fun fetchAthleteStats(id: Long): Result<ActivityStats?>
}