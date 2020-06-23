package com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail

import android.content.Context
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.WorkoutType
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.WorkoutDao
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.WorkoutDatabase
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    fun getWorkouts() : Flowable<List<com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout>> {
            return workoutDao?.getAll()!!.map { mapManualWorkoutToUiWorkout(it) }
    }

    private fun mapManualWorkoutToUiWorkout(workoutList:List<Workout>): List<com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout>{
        val workoutUiObjList = mutableListOf<com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout>()
        workoutList.forEach {
            workoutUiObjList.add(
                com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout(
                    LocalDate.parse(it.workoutDate),
                    "",
                    it.workoutName,
                    WorkoutType.LIFTING,
                    "",
                    "",
                    it.id
                )
            )
        }
        return workoutUiObjList
    }


    suspend fun addWorkout(workout: Workout) {
        manualWorkoutList = listOf()
        withContext(Dispatchers.IO){
            workoutDao?.insertAll(workout)
        }
    }

    suspend fun nukeTable() = workoutDao?.nukeTable()

    //TODO need to expose refresh to invalidate in-mem workouts
}