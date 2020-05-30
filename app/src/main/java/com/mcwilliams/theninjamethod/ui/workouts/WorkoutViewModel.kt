package com.mcwilliams.theninjamethod.ui.workouts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkoutViewModel : ViewModel() {
    private val workoutDate = MutableLiveData<String>()

    fun bind(workoutDateString: String) {
        workoutDate.value = workoutDateString
    }

    fun getWorkoutDate(): MutableLiveData<String> {
        return workoutDate
    }
}