package com.mcwilliams.theninjamethod.network

interface ISessionRepository {
    suspend fun refreshToken(code : String) : String
}