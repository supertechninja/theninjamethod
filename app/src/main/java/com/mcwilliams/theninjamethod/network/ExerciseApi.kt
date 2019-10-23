package com.mcwilliams.theninjamethod.network

import com.mcwilliams.theninjamethod.model.Exercise
import io.reactivex.Observable
import retrofit2.http.GET

interface ExerciseApi {
    @GET("api/v1/h8q6gy97jnzs0")
    fun getExercises(): Observable<List<Exercise>>
}