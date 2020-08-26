package com.mcwilliams.theninjamethod.network.apis

import com.mcwilliams.data.exercisedb.Data
import retrofit2.http.GET

interface ExerciseApi {
    @GET("exercisesApi/exercises")
    suspend fun getExercises(): Data

//
}