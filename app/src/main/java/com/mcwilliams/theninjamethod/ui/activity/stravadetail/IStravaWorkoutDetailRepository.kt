package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import io.reactivex.Observable

interface IStravaWorkoutDetailRepository {
    //Return Detail Strava Activity to render the activity detail
    fun getStravaItemDetail(id: Number): Observable<StravaActivityDetail>
    fun getMultipleStravaDetails(id: List<Number>): Observable<List<StravaActivityDetail>>
    fun mapStravaWorkouts(stravaWorkout: StravaActivityDetail): StravaActivityDetail
}