package com.mcwilliams.theninjamethod.network

import com.mcwilliams.theninjamethod.model.Data
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.model.Response
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExerciseApi {
    @GET("api/v1/h8q6gy97jnzs0")
    fun getExercises(): Observable<List<Exercise>>

    @POST("api/v1/h8q6gy97jnzs0")
    fun addExercise(@Body data: Data): Observable<Response>
}