package com.mcwilliams.theninjamethod.ui.settings

import android.util.Log
import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.appinf.model.Athlete
import com.mcwilliams.settings.model.AthleteStats
import com.mcwilliams.settings.model.StravaAthlete
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(
    private val sessionRepo: SessionRepository,
    private val athleteApi: AthleteApi
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

    override suspend fun fetchAthleteStats(id: String): AthleteStats? =
        withContext(Dispatchers.IO) {
            athleteApi.getAthleteStats(id)
        }

}