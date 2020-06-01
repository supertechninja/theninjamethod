package com.mcwilliams.theninjamethod.network.auth

import com.mcwilliams.theninjamethod.network.SessionRepository
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val sessionRepository: SessionRepository) :
    Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code() == HTTP_UNAUTHORIZED && retryCount(response) == 0) {
            // Retrieve New Access Token from the Session Repository or whatever...
//            val token = sessionRepository.refreshToken()

            return response.request().newBuilder().build()
        } else {
            return null
        }
    }

    private fun retryCount(response: Response?): Int {
        var priorResponse = response
        var result = 0
        while ((priorResponse!!.priorResponse()) != null) {
            priorResponse = priorResponse.priorResponse()
            result++
        }
        return result
    }

}