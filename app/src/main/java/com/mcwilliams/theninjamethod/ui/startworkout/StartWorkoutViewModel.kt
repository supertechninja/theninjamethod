package com.mcwilliams.theninjamethod.ui.startworkout

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.repo.ManualWorkoutsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class StartWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private var _listOfExercises = MutableLiveData<List<Exercise>>()
    var listOfExercises: LiveData<List<Exercise>> = _listOfExercises

    init {
       viewModelScope.launch {
           _listOfExercises.postValue(exerciseRepository.getExercises())
       }
    }

    fun saveWorkout(workout: Workout):Boolean {
        runBlocking { manualWorkoutsRepository.addWorkout(workout) }
        return true
    }


}