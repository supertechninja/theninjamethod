package com.mcwilliams.theninjamethod.strava.api

import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AthleteApi {
    @GET("athlete")
    suspend fun getAthlete(): StravaAthlete

    @GET("athlete/activities")
    fun getAthleteActivities(): Observable<List<ActivitesItem>>

    @GET("activities/{id}")
    fun getActivityDetail(
        @Path("id") id: Number,
        @Query("include_all_efforts") userId: Boolean
    ): Observable<StravaActivityDetail>

//    @GET("athlete/zones")
//    suspend fun getZones(): Zones
}