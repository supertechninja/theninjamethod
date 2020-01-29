package com.mcwilliams.theninjamethod.network

import com.mcwilliams.theninjamethod.model.AddExerciseRequest
import com.mcwilliams.theninjamethod.model.Data
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.model.WorkoutList
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WorkoutApi {

    @GET("exercisesApi/workouts")
    fun getWorkouts(): Observable<WorkoutList>

//    @POST("exercisesApi/exercises")
//    fun addExercise(@Body exercise: AddExerciseRequest): Observable<Exercise>
}