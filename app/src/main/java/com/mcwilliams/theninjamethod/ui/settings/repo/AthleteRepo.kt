package com.mcwilliams.theninjamethod.ui.settings.repo

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete

interface AthleteRepo {
    suspend fun fetchAthlete(): Result<StravaAthlete?>
}