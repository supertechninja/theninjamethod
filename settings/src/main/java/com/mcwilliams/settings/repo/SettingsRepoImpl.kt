package com.mcwilliams.settings.repo

import android.util.Log
import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.appinf.model.Athlete
import com.mcwilliams.settings.AthleteApi
import com.mcwilliams.settings.StravaAthlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(
    private val sessionRepo: SessionRepository,
    private val athleteApi : AthleteApi
) : SettingsRepo {

    override suspend fun authAthlete(code: String): Athlete? = withContext(Dispatchers.IO) {
        val request = sessionRepo.getFirstTokens(code).athlete
        request
    }

    override suspend fun fetchAthlete(): StravaAthlete? = withContext(Dispatchers.IO) {
        val request = athleteApi.getAthlete()
        Log.i("CHRIS", "fetchAthlete has a result")
        request
    }

}