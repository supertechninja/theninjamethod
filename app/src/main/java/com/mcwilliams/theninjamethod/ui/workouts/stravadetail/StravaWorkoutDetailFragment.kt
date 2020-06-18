package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.settings.SettingsViewModel
import com.mcwilliams.theninjamethod.ui.workouts.model.Workout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StravaWorkoutDetailFragment : Fragment() {
    lateinit var workout: Workout

    private val viewModel: StravaDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Workout
        (requireActivity() as AppCompatActivity).supportActionBar!!.title = workout.workoutName
        return inflater.inflate(R.layout.workout_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailedActivity.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "onViewCreated: ${it.name}")
        })

        viewModel.getDetailedActivities(workout.id)

    }
}