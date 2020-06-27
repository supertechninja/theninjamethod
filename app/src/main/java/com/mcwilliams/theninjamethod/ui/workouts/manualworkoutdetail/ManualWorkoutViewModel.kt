package com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.Workout
import kotlinx.coroutines.launch

class ManualWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {

    var _workout: MutableLiveData<Workout> = MutableLiveData()
    var workout: LiveData<Workout> = _workout


    fun getManualWorkoutDetail(id: Number) {
        _workout.postValue(manualWorkoutsRepository.getWorkoutDetail(id))
    }

    fun deleteWorkout() {
        viewModelScope.launch {
            manualWorkoutsRepository.deleteWorkout(workout.value!!)
        }

    }
}