package com.mcwilliams.theninjamethod.network.apis

import com.mcwilliams.theninjamethod.ui.exercises.db.Data
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExerciseApi {
    @GET("exercisesApi/exercises")
    suspend fun getExercises(): Data

//
}