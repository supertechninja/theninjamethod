package com.mcwilliams.theninjamethod.ui.workouts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.model.Workout

class WorkoutViewModel : ViewModel() {
    private val workout = MutableLiveData<Workout>()

    fun bind(workoutObj: Workout) {
        workout.value = workoutObj
    }

    fun getWorkout(): MutableLiveData<Workout> {
        return workout
    }
}