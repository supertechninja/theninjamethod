package com.mcwilliams.theninjamethod.ui.workouts.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.workouts.ui.model.Workout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualWorkoutDetailFragment : Fragment() {
    lateinit var workout:Workout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Workout

        Log.d("TAG", "onCreateView: ${workout.workoutName}")
        return inflater.inflate(R.layout.workout_detail_fragment, container, false)
    }
}