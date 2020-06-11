package com.mcwilliams.theninjamethod.strava

import android.content.Context
import android.content.SharedPreferences
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.api.Session
import com.mcwilliams.theninjamethod.ui.settings.data.TokenResponse
import com.mcwilliams.theninjamethod.ui.settings.repo.AthleteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionRepository @Inject constructor(
    val context: Context,
    private val session: Session
) : ISessionRepository {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    override suspend fun getFirstTokens(code: String): TokenResponse {
        return withContext(context = Dispatchers.IO) {
            val firstToken = session.getFirstToken(
                CLIENT_ID,
                CLIENT_SECRET,
                code,
                GrantType.AUTHORIZATION_CODE.toString()
            )
            if (firstToken.athlete != null) {
//                athleteRepo.saveAthlete(firstToken.athlete)
            }

            setAccessToken(firstToken.access_token)
            setRefreshToken(firstToken.refresh_token)
            firstToken
        }
    }

    override fun refreshToken() {
        val newTokens = session.refreshToken(
            CLIENT_ID,
            CLIENT_SECRET,
            getRefreshToken(),
            GrantType.REFRESH_TOKEN.toString()
        )

        setAccessToken(newTokens.access_token)
        setRefreshToken(newTokens.refresh_token)
    }

    override fun setAccessToken(accessToken: String) {
        with(preferences.edit()) {
            putString(ACCESS_TOKEN, accessToken)
            commit()
        }
    }

    override fun getAccessToken(): String {
        return preferences.getString(ACCESS_TOKEN, "") ?: ""
    }

    override fun setRefreshToken(refreshToken: String) {
        with(preferences.edit()) {
            putString(REFRESH_TOKEN, refreshToken)
            commit()
        }
    }

    override fun getRefreshToken(): String {
        return preferences.getString(REFRESH_TOKEN, "") ?: ""
    }

    override fun setExpiration(expiration: Int) {
        with(preferences.edit()) {
            putInt(EXPIRATION, expiration)
            commit()
        }
    }

    override fun getExpiration(): Int {
        return preferences.getInt(EXPIRATION, 0)
    }

    fun isLoggedIn (): Boolean {
        val doesHaveToken = !preferences.getString(ACCESS_TOKEN, "").isNullOrEmpty()
        val isTokenValid = getExpiration() < System.currentTimeMillis()

        return doesHaveToken && isTokenValid
    }

    companion object {
        const val CLIENT_ID = 47849
        const val CLIENT_SECRET = "bcbe511450ad0d98e32ee8f40ddba379dcae75aa"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val EXPIRATION = "EXPIRATION"
    }

}