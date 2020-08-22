package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.data.ManualWorkoutsRepository
import kotlinx.coroutines.launch

class ManualWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {

    var _workout: MutableLiveData<com.mcwilliams.data.workoutdb.Workout> = MutableLiveData()
    var workout: LiveData<com.mcwilliams.data.workoutdb.Workout> = _workout


    fun getManualWorkoutDetail(id: Number) {
        _workout.postValue(manualWorkoutsRepository.getWorkoutDetail(id))
    }

    fun deleteWorkout() {
        viewModelScope.launch {
            manualWorkoutsRepository.deleteWorkout(workout.value!!)
        }
    }

    fun updateWorkout(workout: com.mcwilliams.data.workoutdb.Workout) {
        viewModelScope.launch {
            manualWorkoutsRepository.updateWorkout(workout)
        }
    }
}