package com.mcwilliams.theninjamethod.strava.api

import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import retrofit2.http.GET

interface AthleteApi {
    @GET("athlete")
    suspend fun getAthlete(): StravaAthlete
    
    @GET("athlete/activities")
    suspend fun getAthleteActivities(): List<ActivitesItem>

//    @GET("athlete/zones")
//    suspend fun getZones(): Zones
}