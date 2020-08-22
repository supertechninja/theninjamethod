package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.data.workoutdb.SimpleWorkout

class WorkoutViewModel : ViewModel() {
    private val workout = MutableLiveData<SimpleWorkout>()
    private lateinit var view: RecyclerView.ViewHolder

    fun bind(workoutObj: SimpleWorkout, holder: RecyclerView.ViewHolder) {
        workout.value = workoutObj
        view = holder
    }

    fun getWorkout(): MutableLiveData<SimpleWorkout> {
        return workout
    }

    fun onWorkoutClicked(){
//        Navigation.findNavController(view.itemView.rootView.rootView).navigate(R.id.navig)
    }
}