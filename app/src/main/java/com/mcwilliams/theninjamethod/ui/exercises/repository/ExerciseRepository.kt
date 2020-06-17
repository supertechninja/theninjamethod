package com.mcwilliams.theninjamethod.ui.exercises.repository

import android.content.Context
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.db.ExerciseDao
import com.mcwilliams.theninjamethod.ui.exercises.db.ExerciseDatabase
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.db.WorkoutDao
import com.mcwilliams.theninjamethod.ui.workouts.db.WorkoutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ExerciseRepository @Inject constructor(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var exerciseDao: ExerciseDao?

    init {
        val db = ExerciseDatabase.getDatabase(context)
        exerciseDao = db?.exerciseDao()
    }

    suspend fun getExercises() = exerciseDao?.getAll()

    suspend fun addExercises(exercise: Exercise) {
        withContext(Dispatchers.IO){
            exerciseDao?.insertAll(exercise)
        }
    }

    suspend fun nukeTable() = exerciseDao?.nukeTable()

}