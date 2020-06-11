package com.mcwilliams.theninjamethod.ui.settings.repo

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.api.Session
import com.mcwilliams.theninjamethod.strava.GrantType
import com.mcwilliams.theninjamethod.strava.SessionRepository
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(private val sessionRepo: SessionRepository) : SettingsRepo {

    override suspend fun authAthlete(code: String): Result<Athlete?> = withContext(Dispatchers.IO) {
        val request = sessionRepo.getFirstTokens(code).athlete
        Result.Success(request)
    }

}