package com.mcwilliams.theninjamethod.strava.api

import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.ActivityStats
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.StravaAthlete
import retrofit2.http.GET
import retrofit2.http.Path

interface AthleteApi {
    @GET("athlete")
    suspend fun getAthlete(): StravaAthlete
    
    @GET("athlete/activities")
    suspend fun getAthleteActivities(): List<ActivitesItem>

    @GET("athletes/{id}/stats")
    suspend fun getActivityStats(@Path("id") id: Long): ActivityStats

//    @GET("athlete/zones")
//    suspend fun getZones(): Zones
}