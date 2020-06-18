package com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.ui.workouts.repo.ManualWorkoutsRepository

class ManualWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {
}