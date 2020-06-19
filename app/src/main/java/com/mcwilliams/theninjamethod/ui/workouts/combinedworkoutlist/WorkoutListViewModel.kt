package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.ui.workouts.model.Workout
import com.mcwilliams.theninjamethod.ui.workouts.model.WorkoutType
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.SessionRepository
import com.mcwilliams.theninjamethod.ui.workouts.repo.ManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.workouts.repo.StravaWorkoutRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class WorkoutListViewModel @ViewModelInject constructor(
    private val sessionRepo: SessionRepository,
    private val stravaWorkoutRepository: StravaWorkoutRepository,
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {

    private val TAG = "WorkoutListViewModel"

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    var isRefreshing: Boolean = false
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadWorkouts() }

    val workoutListAdapter: WorkoutListAdapter =
        WorkoutListAdapter()

    val workoutList: MutableList<Workout> = mutableListOf()

    init {
        loadWorkouts()
    }


    fun onStartWorkoutClick() {
//        manualWorkoutsRepository.addWorkout(createDummyWorkout())
    }

    @SuppressLint("NewApi", "SimpleDateFormat")
    private fun loadWorkouts() {
        viewModelScope.launch {
            val manualWorkouts = manualWorkoutsRepository.getWorkouts()
            if (manualWorkouts!!.isNotEmpty()) {
                manualWorkouts.forEach {
                    workoutList.add(
                        Workout(
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
            }
            //Only render list if not logged in, other list will be rendered
            // after strava activities come back
            if (!sessionRepo.isLoggedIn()) {
                onWorkoutsRetrived()
            }
        }

        if (sessionRepo.isLoggedIn()) {
            viewModelScope.launch {
                try {
                    when (val listOfActivitiesResponse = stravaWorkoutRepository.getStravaActivities()) {
                        is Result.Success -> {
                            listOfActivitiesResponse.data.forEach {
                                workoutList.add(it)
                            }
                            onWorkoutsRetrived()
                        }
                        is Result.Error -> {
//                            _errorMessage.postValue(response.exception.toString())
                        }
                    }
                } catch (e: java.lang.Exception) {
                    if (!e.message.isNullOrEmpty()) {
                        Log.e(TAG, e.message)
                    }
                }
            }
        }
    }

    private fun onWorkoutsRetrived() {
        //Group workouts by date
        val dateKeyedWorkouts: MutableMap<LocalDate, MutableList<Workout>> =
            mutableMapOf()
        val listOfDates = workoutList.distinctBy { it.date }
        for (date in listOfDates) {
            val workoutsByDate: MutableList<Workout> = mutableListOf()
            //TODO chris look at rxjava
            workoutList.forEach {
                if (it.date == date.date) {
                    workoutsByDate.add(it)
                }
            }
            dateKeyedWorkouts[date.date] = workoutsByDate
        }
        updateListView(dateKeyedWorkouts.toList())
        loadingVisibility.value = View.GONE
    }

    fun onWorkoutClicked(workout: Workout) {

    }

    fun getMiles(meters: Float): Double {
        return meters * 0.000621371192;
    }

    fun dropWorkoutDb() {
        viewModelScope.launch {
            manualWorkoutsRepository.nukeTable()
        }
    }

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

    private fun updateListView(dateKeyedWorkouts: List<Pair<LocalDate, MutableList<Workout>>>) {
        loadingVisibility.value = View.GONE
        workoutListAdapter.updateWorkoutList(dateKeyedWorkouts)
    }

    private fun onRetrievePostListError() {
//        errorMessage.value = R.string.exercise_error
    }
}