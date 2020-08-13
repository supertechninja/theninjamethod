package com.mcwilliams.settings

import retrofit2.http.GET

interface AthleteApi {
    @GET("athlete")
    suspend fun getAthlete(): StravaAthlete

}