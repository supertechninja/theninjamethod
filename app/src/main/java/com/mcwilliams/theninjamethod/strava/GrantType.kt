package com.mcwilliams.theninjamethod.strava

enum class GrantType (private val rawValue: String){
    AUTHORIZATION_CODE("authorization_code"), REFRESH_TOKEN("refresh_token");

    override fun toString(): String {
        return rawValue
    }
}