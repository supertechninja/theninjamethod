package com.mcwilliams.theninjamethod.network.apis

import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.WorkoutList
import retrofit2.http.GET

interface WorkoutApi {
    @GET("exercisesApi/workouts")
    suspend fun getWorkouts(): WorkoutList
}