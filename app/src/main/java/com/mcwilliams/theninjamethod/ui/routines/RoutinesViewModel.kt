package com.mcwilliams.theninjamethod.ui.routines

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import io.reactivex.disposables.CompositeDisposable

class RoutinesViewModel @ViewModelInject constructor(
    private val routinesRepository: RoutinesRepository
) : ViewModel() {
    val rootDisposable = CompositeDisposable()
    var _workout: MutableLiveData<List<com.mcwilliams.data.workoutdb.Workout>> = MutableLiveData()
    var workout: LiveData<List<com.mcwilliams.data.workoutdb.Workout>> = _workout

    init {
        workout =
            routinesRepository.getWorkouts().toObservable().toLiveData(rootDisposable) { it }
//            _workout.postValue(routinesRepository.getWorkouts())
    }


//    fun deleteWorkout() {
//        viewModelScope.launch {
//            routinesRepository.deleteWorkout(workout.value!!)
//        }
//
//    }
}