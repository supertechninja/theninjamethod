package com.mcwilliams.theninjamethod.network

import com.mcwilliams.theninjamethod.model.*
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExerciseApi {
    @GET("exercisesApi/exercises")
    fun getExercises(): Observable<Data>

    @POST("exercisesApi/exercises")
    fun addExercise(@Body exercise: AddExerciseRequest): Observable<Exercise>
}