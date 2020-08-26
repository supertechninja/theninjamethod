package com.mcwilliams.theninjamethod.network.apis

import com.mcwilliams.data.workoutdb.WorkoutList
import retrofit2.http.GET

interface WorkoutApi {
    @GET("exercisesApi/workouts")
    suspend fun getWorkouts(): WorkoutList
}