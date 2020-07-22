package com.mcwilliams.theninjamethod.ui.exercises.repository

import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import io.reactivex.Flowable

interface IExerciseRepository {
    fun getExercises(): Flowable<List<Exercise>>?

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun getRemoteExercises()

    suspend fun addExercises(exercise: Exercise)

    suspend fun nukeTable()
}