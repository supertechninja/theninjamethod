package com.mcwilliams.theninjamethod.ui.exercises.repository

import android.content.Context
import android.content.SharedPreferences
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.network.apis.ExerciseApi
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.db.ExerciseDao
import com.mcwilliams.theninjamethod.ui.exercises.db.ExerciseDatabase
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.db.WorkoutDao
import com.mcwilliams.theninjamethod.ui.workouts.db.WorkoutDatabase
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ExerciseRepository @Inject constructor(
    val context: Context,
    private val exerciseApi: ExerciseApi
) : CoroutineScope {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var exerciseDao: ExerciseDao?

    init {
        val db = ExerciseDatabase.getDatabase(context)
        exerciseDao = db?.exerciseDao()
        if (!preferences.getBoolean("hasRetrievedExercises", false)) {
            CoroutineScope(coroutineContext).launch { getRemoteExercises() }
        }
    }

    //On initialization of the repository fetch remote exercises to be stored
    private suspend fun getRemoteExercises() {
        val data = exerciseApi.getExercises()
        data.exercises.forEach {
            runBlocking { addExercises(it) }
        }
        preferences.edit().putBoolean("hasRetrievedExercises", true).apply()
    }

    suspend fun getExercises() = exerciseDao?.getAll()

    suspend fun addExercises(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            exerciseDao?.insertAll(exercise)
        }
    }

    suspend fun nukeTable() = exerciseDao?.nukeTable()

}