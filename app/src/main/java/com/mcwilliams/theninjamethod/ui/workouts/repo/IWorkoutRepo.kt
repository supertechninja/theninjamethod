package com.mcwilliams.theninjamethod.ui.workouts.repo

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.ui.workouts.model.Workout

interface IWorkoutRepo {
    suspend fun getStravaActivities(): Result<List<Workout>>
}