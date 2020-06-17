package com.mcwilliams.theninjamethod.ui.workouts.repo

import android.content.Context
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.db.WorkoutDao
import com.mcwilliams.theninjamethod.ui.workouts.db.WorkoutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ManualWorkoutsRepository @Inject constructor(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var workoutDao: WorkoutDao?

    init {
        val db = WorkoutDatabase.getDatabase(context)
        workoutDao = db?.workoutDao()
    }

    fun getWorkouts() = workoutDao?.getAll()

    fun addWorkout(workout: Workout) {
        launch  { setWorkout(workout) }
    }

    private suspend fun setWorkout(workout: Workout){
        withContext(Dispatchers.IO){
            workoutDao?.insertAll(workout)
        }
    }

}