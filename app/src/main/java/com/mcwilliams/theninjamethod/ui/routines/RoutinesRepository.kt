package com.mcwilliams.theninjamethod.ui.routines

import android.content.Context
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.WorkoutType
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout
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
    private var manualWorkoutList = listOf<Workout>()

    init {
        val db = RoutinesDatabase.getDatabase(context)
        routineDao = db?.routineDao()
    }

    //check cache workouts before reading db (reading db is heavy)
    fun getWorkouts(): Flowable<List<Workout>> {
        return routineDao?.getAll()!!
    }

    fun getWorkoutDetail(id: Number): Workout? {
        return manualWorkoutList.find { it.id == id }
    }

    suspend fun deleteWorkout(workout: Workout) {
        withContext(Dispatchers.IO) {
            routineDao?.delete(workout)
        }
    }

    private fun mapManualWorkoutToUiWorkout(workoutList: List<Workout>): List<com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout> {
        val workoutUiObjList =
            mutableListOf<com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout>()
        manualWorkoutList = workoutList
        workoutList.forEach {
            workoutUiObjList.add(
                com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout(
                    LocalDate.parse(it.workoutDate),
                    "",
                    it.workoutName,
                    WorkoutType.LIFTING,
                    "27,000lbs",
                    "50:00",
                    it.id
                )
            )
        }
        return workoutUiObjList
    }


    suspend fun addRoutine(routine: Workout) {
        withContext(Dispatchers.IO) {
            routineDao?.insertAll(routine)
        }
    }

    suspend fun nukeTable() = routineDao?.nukeTable()

    //TODO need to expose refresh to invalidate in-mem workouts
}