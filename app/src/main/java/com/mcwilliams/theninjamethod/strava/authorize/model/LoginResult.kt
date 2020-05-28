package com.mcwilliams.theninjamethod.strava.authorize.model

data class LoginResult(
    val access_token: String,
    val expires_at: Int,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)