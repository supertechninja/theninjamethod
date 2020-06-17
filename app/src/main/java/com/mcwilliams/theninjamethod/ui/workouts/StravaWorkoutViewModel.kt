package com.mcwilliams.theninjamethod.ui.workouts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.model.Workout

class StravaWorkoutViewModel : ViewModel() {
    private lateinit var stravaWorkout : Pair<String, MutableList<Workout>>
    private lateinit var view : RecyclerView.ViewHolder

    fun bind(workoutObj: Pair<String, MutableList<Workout>>, holder: RecyclerView.ViewHolder) {
        stravaWorkout = workoutObj
        view = holder
    }

    fun getStravaWorkout(): Pair<String, MutableList<Workout>> {
        return stravaWorkout
    }

}