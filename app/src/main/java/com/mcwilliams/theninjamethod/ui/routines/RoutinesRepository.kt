package com.mcwilliams.theninjamethod.ui.routines

import android.content.Context
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.data.workoutdb.Workout
import com.mcwilliams.data.workoutdb.WorkoutType
import com.mcwilliams.theninjamethod.ui.routines.db.RoutineDao
import com.mcwilliams.theninjamethod.ui.routines.db.RoutinesDatabase
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RoutinesRepository @Inject constructor(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var routineDao: RoutineDao?

    //in memory cache of workouts from db
    private var manualWorkoutList = listOf<com.mcwilliams.data.workoutdb.Workout>()

    init {
        val db = RoutinesDatabase.getDatabase(context)
        routineDao = db?.routineDao()
    }

    //check cache workouts before reading db (reading db is heavy)
    fun getWorkouts(): Flowable<List<com.mcwilliams.data.workoutdb.Workout>> {
        return routineDao?.getAll()!!
    }

    fun getWorkoutDetail(id: Number): com.mcwilliams.data.workoutdb.Workout? {
        return manualWorkoutList.find { it.id == id }
    }

    suspend fun deleteWorkout(workout: com.mcwilliams.data.workoutdb.Workout) {
        withContext(Dispatchers.IO) {
            routineDao?.delete(workout)
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
                    "27,000lbs",
                    "50:00",
                    "",
                    it.id
                )
            )
        }
        return workoutUiObjList
    }


    suspend fun addRoutine(routine: com.mcwilliams.data.workoutdb.Workout) {
        withContext(Dispatchers.IO) {
            routineDao?.insertAll(routine)
        }
    }

    suspend fun nukeTable() = routineDao?.nukeTable()

    //TODO need to expose refresh to invalidate in-mem workouts
}