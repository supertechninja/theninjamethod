package com.mcwilliams.theninjamethod.ui.settings.repo

import android.util.Log
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.api.Session
import com.mcwilliams.theninjamethod.strava.GrantType
import com.mcwilliams.theninjamethod.strava.SessionRepository
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(
    private val sessionRepo: SessionRepository,
    private val athleteApi : AthleteApi
) : SettingsRepo {

    override suspend fun authAthlete(code: String): Result<Athlete?> = withContext(Dispatchers.IO) {
        val request = sessionRepo.getFirstTokens(code).athlete
        Result.Success(request)
    }

    override suspend fun fetchAthlete(): Result<StravaAthlete?> = withContext(Dispatchers.IO) {
        val request = athleteApi.getAthlete()
        Log.i("CHRIS", "fetchAthlete has a result")
        Result.Success(request)
    }

}