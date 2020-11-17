package com.mcwilliams.theninjamethod.strava.api

import com.mcwilliams.settings.model.AthleteStats
import com.mcwilliams.settings.model.StravaAthlete
import retrofit2.http.GET
import retrofit2.http.Path

interface AthleteApi {
    @GET("athlete")
    suspend fun getAthlete(): StravaAthlete

    @GET("athletes/{id}/stats")
    suspend fun getAthleteStats(@Path("id") id: String): AthleteStats

}