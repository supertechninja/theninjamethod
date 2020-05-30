package com.mcwilliams.theninjamethod.ui.settings.repo

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.network.TokenApi
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete
import com.mcwilliams.theninjamethod.ui.settings.data.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(
    private val stravaApi: TokenApi
) : SettingsRepo {

    override suspend fun authAthlete(code:String): Result<Athlete> = withContext(Dispatchers.IO) {
            val request =
                stravaApi.token(CLIENT_ID, CLIENT_SECRET, code).athlete
            Result.Success(request)
        }

//        return

//        val result = stravaApi.token(CLIENT_ID, CLIENT_SECRET, code)
//        if (result is Result.Success) {
//            val athlete = result.data.athlete
//        }
//        return result!!



    companion object {
        const val CLIENT_ID = 47849
        const val CLIENT_SECRET = "bcbe511450ad0d98e32ee8f40ddba379dcae75aa"
    }
}