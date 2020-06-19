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

    //in memory cache of workouts from db
    private var manualWorkoutList = listOf<Workout>()

    init {
        val db = WorkoutDatabase.getDatabase(context)
        workoutDao = db?.workoutDao()
    }

    //check cache workouts before reading db (reading db is heavy)
    suspend fun getWorkouts() : List<Workout> {
        if (manualWorkoutList.isEmpty()) {
            val workoutList = workoutDao?.getAll()
            if (!workoutList.isNullOrEmpty()) {
                manualWorkoutList = workoutList!!
            }
        }
        return manualWorkoutList
    }

    suspend fun addWorkout(workout: Workout) {
        withContext(Dispatchers.IO){
            workoutDao?.insertAll(workout)
        }
    }

    suspend fun nukeTable() = workoutDao?.nukeTable()

    //TODO need to expose refresh to invalidate in-mem workouts
}