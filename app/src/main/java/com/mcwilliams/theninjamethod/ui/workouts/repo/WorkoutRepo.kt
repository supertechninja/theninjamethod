package com.mcwilliams.theninjamethod.ui.workouts.repo

import android.content.Context
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.ui.workouts.repo.IWorkoutRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutRepo @Inject constructor(val context: Context, private val athleteApi: AthleteApi)  :
    IWorkoutRepo {

    override suspend fun getStravaActivities(): Result<List<ActivitesItem>> = withContext(
        Dispatchers.IO) {
        val request = athleteApi.getAthleteActivities()
        Result.Success(request)
    }
}