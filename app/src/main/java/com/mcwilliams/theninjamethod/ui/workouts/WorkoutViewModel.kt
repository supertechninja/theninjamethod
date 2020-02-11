package com.mcwilliams.theninjamethod.ui.workouts

import androidx.lifecycle.MutableLiveData
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.model.Workout
import com.mcwilliams.theninjamethod.utils.viewmodel.BaseViewModel

class WorkoutViewModel : BaseViewModel() {
    private val workoutDate = MutableLiveData<String>()

    fun bind(workoutDateString: String) {
        workoutDate.value = workoutDateString
    }

    fun getWorkoutDate(): MutableLiveData<String> {
        return workoutDate
    }
}