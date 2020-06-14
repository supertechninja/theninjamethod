package com.mcwilliams.theninjamethod.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mcwilliams.theninjamethod.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.workout_detail_fragment, container)
    }



}