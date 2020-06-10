package com.mcwilliams.theninjamethod.ui.settings.data

data class TokenResponse(
    val access_token: String,
    val athlete: Athlete?,
    val expires_at: Int,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)