package com.mcwilliams.theninjamethod.strava.api

import com.mcwilliams.theninjamethod.strava.model.DetailedAthlete
import com.mcwilliams.theninjamethod.strava.model.Zones
import retrofit2.http.GET

interface AthleteApi {
    @GET("athlete")
    suspend fun getAthlete(): DetailedAthlete

    @GET("athlete/zones")
    suspend fun getZones(): Zones
}