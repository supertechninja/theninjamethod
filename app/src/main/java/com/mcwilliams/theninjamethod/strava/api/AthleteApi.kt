package com.mcwilliams.theninjamethod.strava.api

import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import retrofit2.http.GET

interface AthleteApi {
    @GET("athlete")
    suspend fun getAthlete(): StravaAthlete

//    @GET("athlete/zones")
//    suspend fun getZones(): Zones
}