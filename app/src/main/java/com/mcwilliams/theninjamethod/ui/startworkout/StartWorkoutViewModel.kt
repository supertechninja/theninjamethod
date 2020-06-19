package com.mcwilliams.theninjamethod.ui.startworkout

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.ManualWorkoutsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StartWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private var _listOfExercises = MutableLiveData<List<Exercise>>()
    var listOfExercises: LiveData<List<Exercise>> = _listOfExercises

    private var _didSaveWorkout = MutableLiveData<Boolean>()
    var didSaveWorkout: LiveData<Boolean> = _didSaveWorkout

    init {
       //Preload list of exercises from exerciseDB
       viewModelScope.launch {
           _listOfExercises.postValue(exerciseRepository.getExercises())
       }
    }

    fun saveWorkout(workout: Workout){
        viewModelScope.launch {
            manualWorkoutsRepository.addWorkout(workout)
            _didSaveWorkout.postValue(true)
        }
    }


}