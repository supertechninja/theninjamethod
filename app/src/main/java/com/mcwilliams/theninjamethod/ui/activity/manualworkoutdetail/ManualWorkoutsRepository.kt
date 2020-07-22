package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail

import android.content.Context
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.WorkoutType
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.WorkoutDao
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.WorkoutDatabase
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ManualWorkoutsRepository @Inject constructor(val context: Context) : CoroutineScope,
    IManualWorkoutsRepository {

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
    override fun getWorkouts(): Flowable<List<com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout>> {
        return workoutDao?.getAll()!!.map { mapManualWorkoutToUiWorkout(it) }
    }

    override fun getWorkoutDetail(id: Number): Workout? {
        return manualWorkoutList.find { it.id == id }
    }

    override suspend fun deleteWorkout(workout: Workout) {
        withContext(Dispatchers.IO) {
            workoutDao?.delete(workout)
        }
    }

    override fun mapManualWorkoutToUiWorkout(workoutList: List<Workout>): List<com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout> {
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
                    "${it.workoutTotalWeight!!} lbs",
                    it.caloriesBurned ?: "",
                    it.workoutDuration ?: "",
                    it.id
                )
            )
        }
        return workoutUiObjList
    }


    override suspend fun addWorkout(workout: Workout) {
        manualWorkoutList = listOf()
        withContext(Dispatchers.IO) {
            workoutDao?.insertAll(workout)
        }
    }

    override suspend fun nukeTable() = workoutDao?.nukeTable()

    //TODO need to expose refresh to invalidate in-mem workouts
}