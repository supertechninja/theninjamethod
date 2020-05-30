package com.mcwilliams.theninjamethod.network

import com.mcwilliams.theninjamethod.model.WorkoutList
import retrofit2.http.GET

interface WorkoutApi {
    @GET("exercisesApi/workouts")
    suspend fun getWorkouts(): WorkoutList
}