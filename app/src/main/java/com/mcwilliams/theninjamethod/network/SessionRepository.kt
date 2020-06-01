package com.mcwilliams.theninjamethod.network

import com.mcwilliams.theninjamethod.network.apis.TokenApi
import com.mcwilliams.theninjamethod.strava.StravaLogin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.log

class SessionRepository @Inject constructor(val tokenApi: TokenApi) : ISessionRepository {
    override suspend fun refreshToken(code: String): String {
        return withContext(context = Dispatchers.IO) {
            tokenApi.token(CLIENT_ID, CLIENT_SECRET, code).access_token
        }
    }

    companion object {
        const val CLIENT_ID = 47849
        const val CLIENT_SECRET = "bcbe511450ad0d98e32ee8f40ddba379dcae75aa"
    }

}