package com.mcwilliams.theninjamethod.ui.workouts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.workouts.ui.model.Workout

class WorkoutViewModel : ViewModel() {
    private val workout = MutableLiveData<Workout>()
    private lateinit var view : RecyclerView.ViewHolder

    fun bind(workoutObj: Workout, holder: RecyclerView.ViewHolder) {
        workout.value = workoutObj
        view = holder
    }

    fun getWorkout(): MutableLiveData<Workout> {
        return workout
    }

    fun onWorkoutClicked(){
        Navigation.findNavController(view.itemView.rootView.rootView).navigate(R.id.navigate_to_workout_detail)
    }
}