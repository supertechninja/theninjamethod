package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail

import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout
import io.reactivex.Flowable

interface IManualWorkoutsRepository {
    //check cache workouts before reading db (reading db is heavy)
    fun getWorkouts(): Flowable<List<Workout>>
    fun getWorkoutDetail(id: Number): com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout?

    suspend fun deleteWorkout(workout: com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout)
    fun mapManualWorkoutToUiWorkout(workoutList: List<com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout>): List<Workout>

    suspend fun addWorkout(workout: com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout)

    suspend fun nukeTable(): Unit?
}