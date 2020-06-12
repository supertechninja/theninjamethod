package com.mcwilliams.theninjamethod.ui.workouts

import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.model.*
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.network.apis.WorkoutApi
import com.mcwilliams.theninjamethod.strava.SessionRepository
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            val data = workoutApi.getWorkouts()
            onRetrievePostListSuccess(data)
        }

        if (sessionRepo.isLoggedIn()) {
            viewModelScope.launch {
                try {
                    when (val listOfActivitiesResponse = workoutRepo.getStravaActivities()) {
                        is Result.Success -> {
                            val listOfActivities =  listOfActivitiesResponse.data
                            Log.d(TAG, "loadWorkouts: ${listOfActivities.size}")
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

//    fun deleteExercise(position : Int){
//        val exerciseToDelete = exerciseListAdapter.getExerciseList()[position]
//    }

    //    fun addExercise(exercise: AddExerciseRequest){
//        subscription = exerciseApi.addExercise(exercise)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { refreshData() },
//                { onRetrievePostListError() }
//            )
//
//    }
//
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

    private fun onRetrievePostListSuccess(workoutList: WorkoutList) {
        Log.d(TAG, "onRetrievePostListSuccess: " + workoutList.workouts.size.toString())
        loadingVisibility.value = View.GONE
        workoutListAdapter.updateWorkoutList(
            workoutList.workouts.associateBy(
                keySelector = { it.date },
                valueTransform = { it }).keys.toList()
        )
    }

    private fun onRetrievePostListError() {
//        errorMessage.value = R.string.exercise_error
    }
}