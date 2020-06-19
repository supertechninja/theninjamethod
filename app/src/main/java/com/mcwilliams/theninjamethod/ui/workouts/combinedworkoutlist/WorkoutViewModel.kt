package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.ui.workouts.model.Workout

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
//        Navigation.findNavController(view.itemView.rootView.rootView).navigate(R.id.navig)
    }
}