package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout
import io.reactivex.Observable

interface IStravaWorkoutRepository {
    fun getStravaActivities(): Observable<List<Workout>>

    //Return a list of strava workout summaries, a subset of the detail data needed to show the ui
    fun mapStravaWorkouts(stravaWorkouts: List<ActivitesItem>): List<Workout>
}