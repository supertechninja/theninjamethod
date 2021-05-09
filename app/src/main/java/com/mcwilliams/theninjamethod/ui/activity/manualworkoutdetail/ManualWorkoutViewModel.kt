package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.data.ManualWorkoutsRepository
import com.mcwilliams.data.workoutdb.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManualWorkoutViewModel @Inject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {

    var _workout: MutableLiveData<Workout> = MutableLiveData()
    var workout: LiveData<Workout> = _workout
    lateinit var currentWorkoutId: Number

    fun getManualWorkoutDetail(id: Number) {
        currentWorkoutId = id
        _workout.postValue(manualWorkoutsRepository.getWorkoutDetail(currentWorkoutId))
    }

    fun deleteWorkout() {
        viewModelScope.launch {
            manualWorkoutsRepository.deleteWorkout(workout.value!!)
        }
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            manualWorkoutsRepository.updateWorkout(workout)
            _workout.postValue(manualWorkoutsRepository.getWorkoutDetail(currentWorkoutId))
        }
    }
}