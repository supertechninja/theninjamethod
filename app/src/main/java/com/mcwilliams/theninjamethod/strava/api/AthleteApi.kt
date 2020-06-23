package com.mcwilliams.theninjamethod.strava.api

import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import io.reactivex.Observable
import retrofit2.http.GET

interface AthleteApi {
    @GET("athlete")
    suspend fun getAthlete(): StravaAthlete
    
    @GET("athlete/activities")
    fun getAthleteActivities(): Observable<List<ActivitesItem>>

//    @GET("athlete/zones")
//    suspend fun getZones(): Zones
}