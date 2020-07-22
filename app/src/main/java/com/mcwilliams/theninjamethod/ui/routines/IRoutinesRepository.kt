package com.mcwilliams.theninjamethod.ui.routines

import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout
import io.reactivex.Flowable

interface IRoutinesRepository {
    //check cache workouts before reading db (reading db is heavy)
    fun getWorkouts(): Flowable<List<Workout>>
    fun getWorkoutDetail(id: Number): Workout?

    suspend fun deleteWorkout(workout: Workout)
    fun mapManualWorkoutToUiWorkout(workoutList: List<Workout>): List<com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout>

    suspend fun addRoutine(routine: Workout)

    suspend fun nukeTable(): Unit?
}