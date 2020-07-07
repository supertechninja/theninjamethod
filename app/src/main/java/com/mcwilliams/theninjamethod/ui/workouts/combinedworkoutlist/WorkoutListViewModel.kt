package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.strava.SessionRepository
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.ManualWorkoutsRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.collections.set

class WorkoutListViewModel @ViewModelInject constructor(
    sessionRepo: SessionRepository,
    stravaWorkoutRepository: StravaWorkoutRepository,
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {
    var isRefreshing: Boolean = false
    val errorMessage: MutableLiveData<Int> = MutableLiveData()

    val rootDisposable = CompositeDisposable()

    var _workoutMapLiveData: MutableLiveData<MutableList<Pair<LocalDate, MutableList<Workout>>>> =
        MutableLiveData()
    var workoutMapLiveData: LiveData<MutableList<Pair<LocalDate, MutableList<Workout>>>> =
        _workoutMapLiveData

    var _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.postValue(true)
        if (sessionRepo.isLoggedIn()) {
            workoutMapLiveData =
                Observable.combineLatest(
                    stravaWorkoutRepository.getStravaActivities(),
                    manualWorkoutsRepository.getWorkouts().toObservable(),
                    BiFunction<List<Workout>, List<Workout>, List<Workout>> { strava, manual
                        ->
                        mutableListOf(strava, manual).flatten()
                    })
                    .map { onWorkoutsRetrived(it) }
                    .toLiveData(rootDisposable) { it }

        } else {
            workoutMapLiveData =
                manualWorkoutsRepository.getWorkouts().toObservable().map { onWorkoutsRetrived(it) }
                    .toLiveData(rootDisposable) { it }
        }
    }

    private fun onWorkoutsRetrived(wrkOutList: List<Workout>): MutableList<Pair<LocalDate, MutableList<Workout>>> {
        //Group workouts by date to return
        val dateKeyedWorkouts: MutableMap<LocalDate, MutableList<Workout>> = mutableMapOf()
        //sort the workout by date ascending
        val sortedWorkouts = wrkOutList.sortedByDescending { it.date }
        //get workouts by unique date
        val listOfDates = sortedWorkouts.distinctBy { it.date }

        //creates a 1-date to many workout list
        for (date in listOfDates) {
            val workoutsByDate: MutableList<Workout> = mutableListOf()
            sortedWorkouts.forEach {
                if (it.date == date.date) {
                    workoutsByDate.add(it)
                }
            }
            dateKeyedWorkouts[date.date] = workoutsByDate
        }
        return dateKeyedWorkouts.toList().toMutableList()
    }

    fun dropWorkoutDb() {
        viewModelScope.launch {
            manualWorkoutsRepository.nukeTable()
        }
    }

    fun refreshData() {
//        workoutList.clear()
//        workoutListAdapter.clear()
//        workoutListAdapter.notifyDataSetChanged()
        isRefreshing = true
    }

    private fun onRetrievePostListError() {
//        errorMessage.value = R.string.exercise_error
    }
}