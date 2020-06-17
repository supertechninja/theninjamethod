package com.mcwilliams.theninjamethod.ui.startworkout

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.repo.ManualWorkoutsRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class StartWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {


    fun saveWorkout(workout: Workout):Boolean {
        runBlocking { manualWorkoutsRepository.addWorkout(workout) }
        return true
    }


}