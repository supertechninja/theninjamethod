package com.mcwilliams.theninjamethod.strava

import android.content.Context

class StravaLogin(private val context: Context?) {
    private var clientID = 0
    private var redirectURI: String? = null
    private var approvalPrompt: ApprovalPrompt? = null
    private var accessScope: AccessScope? = null
    fun withClientID(clientID: Int): StravaLogin {
        this.clientID = clientID
        return this
    }

    fun withRedirectURI(redirectURI: String?): StravaLogin {
        this.redirectURI = redirectURI
        return this
    }

    fun withApprovalPrompt(approvalPrompt: ApprovalPrompt?): StravaLogin {
        this.approvalPrompt = approvalPrompt
        return this
    }

    fun withAccessScope(accessScope: AccessScope?): StravaLogin {
        this.accessScope = accessScope
        return this
    }

    fun makeLoginURL(): String {
        val loginURLBuilder = StringBuilder()
        loginURLBuilder.append(STRAVA_LOGIN_URL)
        loginURLBuilder.append(clientIDParameter())
        loginURLBuilder.append(redirectURIParameter())
        loginURLBuilder.append(approvalPromptParameter())
        loginURLBuilder.append(accessScopeParameter())
        return loginURLBuilder.toString()
    }

    private fun clientIDParameter(): String {
        return "&client_id=$clientID"
    }

    private fun redirectURIParameter(): String {
        return if (redirectURI != null) {
            "&redirect_uri=$redirectURI"
        } else {
            ""
        }
    }

    private fun approvalPromptParameter(): String {
        return if (approvalPrompt != null) {
            "&approval_prompt=" + approvalPrompt.toString()
        } else {
            ""
        }
    }

    private fun accessScopeParameter(): String {
        return if (accessScope != null) {
            "&scope=" + accessScope.toString()
        } else {
            ""
        }
    }

    companion object {
        private const val STRAVA_LOGIN_URL =
            "https://www.strava.com/oauth/authorize?response_type=code"

        fun withContext(context: Context?): StravaLogin {
            return StravaLogin(context)
        }
    }

}