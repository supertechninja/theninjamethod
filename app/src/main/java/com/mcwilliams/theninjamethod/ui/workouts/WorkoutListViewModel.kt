package com.mcwilliams.theninjamethod.ui.workouts

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.model.Workout
import com.mcwilliams.theninjamethod.model.WorkoutType
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.network.apis.WorkoutApi
import com.mcwilliams.theninjamethod.strava.SessionRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WorkoutListViewModel @ViewModelInject constructor(
    private val workoutApi: WorkoutApi,
    private val sessionRepo: SessionRepository,
    private val workoutRepo: WorkoutRepo
) : ViewModel() {

    private val TAG = "WorkoutListViewModel"

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    var isRefreshing: Boolean = false
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadWorkouts() }

    val workoutListAdapter: WorkoutListAdapter = WorkoutListAdapter()

    val workoutList: MutableList<Workout> = mutableListOf()

    init {
        loadWorkouts()
    }

    @SuppressLint("NewApi", "SimpleDateFormat")
    private fun loadWorkouts() {
        viewModelScope.launch {
            val data = workoutApi.getWorkouts()
            data.workouts.forEach {
                it.workoutType = WorkoutType.LIFTING
            }
            workoutList.addAll(data.workouts)
            isRefreshing = false
            updateListView()
        }

        if (sessionRepo.isLoggedIn()) {
            viewModelScope.launch {
                try {
                    when (val listOfActivitiesResponse = workoutRepo.getStravaActivities()) {
                        is Result.Success -> {
                            val listOfActivities =  listOfActivitiesResponse.data
                            Log.d(TAG, "loadWorkouts: ${listOfActivities.size}")
                            listOfActivities.forEach {
                                val dtf = DateTimeFormatter.ISO_DATE_TIME
                                val zdt: ZonedDateTime = ZonedDateTime.parse(it.start_date_local, dtf)
                                val localDateTime = zdt.toLocalDateTime()
                                val date = localDateTime.toLocalDate()
                                val time = localDateTime.toLocalTime()


                                val elapsedTime = "${it.elapsed_time / 60}:${it.elapsed_time % 60} min"
                                val miles = "${getMiles(it.distance).round(2).toString()} mi"

                                val workoutItem = Workout("$date $time", it.name, WorkoutType.STRAVA, miles, elapsedTime)
                                workoutList.add(workoutItem)
                            }
                            updateListView()
                        }
                        is Result.Error -> {
//                            _errorMessage.postValue(response.exception.toString())
                        }
                    }
                } catch (e: java.lang.Exception) {
//                    _errorMessage.postValue(e.message)
                }
            }
        }
    }

    fun onWorkoutClicked(workout: Workout){

    }

    fun getMiles(meters:Float): Double {
        return meters*0.000621371192;
    }

    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

    fun refreshData() {
        isRefreshing = true
        loadWorkouts()
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
        isRefreshing = false
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
        isRefreshing = false
    }

    private fun updateListView() {
        loadingVisibility.value = View.GONE
        workoutListAdapter.updateWorkoutList(
            workoutList.associateBy(
                keySelector = { it },
                valueTransform = { it }).keys.toList()
        )
    }

    private fun onRetrievePostListError() {
//        errorMessage.value = R.string.exercise_error
    }
}