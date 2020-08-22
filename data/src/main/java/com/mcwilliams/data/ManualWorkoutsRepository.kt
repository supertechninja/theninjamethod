package com.mcwilliams.data

import android.content.Context
import com.mcwilliams.data.workoutdb.*
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
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
    fun getWorkouts(): Flowable<List<SimpleWorkout>> {
        return workoutDao?.getAll()!!.map { mapManualWorkoutToUiWorkout(it) }
    }

    fun getWorkoutDetail(id: Number): Workout? {
        return manualWorkoutList.find { it.id == id }
    }

    suspend fun deleteWorkout(workout: Workout) {
        withContext(Dispatchers.IO) {
            workoutDao?.delete(workout)
        }
    }

    suspend fun updateWorkout(workout: Workout) {
        withContext(Dispatchers.IO) {
            workoutDao?.updateWorkout(workout)
        }
    }

    private fun mapManualWorkoutToUiWorkout(workoutList: List<Workout>): List<SimpleWorkout> {
        val workoutUiObjList =
            mutableListOf<SimpleWorkout>()
        manualWorkoutList = workoutList
        workoutList.forEach {
            workoutUiObjList.add(
                SimpleWorkout(
                    LocalDate.parse(it.workoutDate),
                    "",
                    it.workoutName,
                    WorkoutType.LIFTING,
                    "${it.workoutTotalWeight!!} lbs",
                    it.caloriesBurned ?: "",
                    it.workoutDuration ?: "",
                    it.id
                )
            )
        }
        return workoutUiObjList
    }


    suspend fun addWorkout(workout: Workout) {
        manualWorkoutList = listOf()
        withContext(Dispatchers.IO) {
            workoutDao?.insertAll(workout)
        }
    }

    suspend fun nukeTable() = workoutDao?.nukeTable()

    //TODO need to expose refresh to invalidate in-mem workouts
}