package com.mcwilliams.theninjamethod.ui.workouts.repo

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem

interface IWorkoutRepo {
    suspend fun getStravaActivities(): Result<List<ActivitesItem>>
}