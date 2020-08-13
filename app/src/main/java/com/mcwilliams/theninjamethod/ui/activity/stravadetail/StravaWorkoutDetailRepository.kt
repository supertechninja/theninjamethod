package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import com.mcwilliams.theninjamethod.strava.api.ActivitiesApi
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.utils.extensions.*
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StravaWorkoutDetailRepository @Inject constructor(
    private val activitiesApi: ActivitiesApi
) {
    //Return Detail Strava Activity to render the activity detail
    fun getStravaItemDetail(id: Number): Observable<StravaActivityDetail> {
        return activitiesApi.getActivityDetail(id, true).map { mapStravaWorkouts(it) }
    }

    fun getMultipleStravaDetails(id: List<Number>): Observable<List<StravaActivityDetail>> {
        return Observable.zip(
            getStravaItemDetail(id[0]),
            getStravaItemDetail(id[1]),
            BiFunction { detail1, detail2 -> mutableListOf(detail1, detail2) })
    }

    private fun mapStravaWorkouts(stravaWorkout: StravaActivityDetail): StravaActivityDetail {
        val date = stravaWorkout.start_date_local.getDate()
        val time = stravaWorkout.start_date_local.getTime()

        val movingTime = "${stravaWorkout.moving_time / 60}m ${stravaWorkout.moving_time % 60}s"
        val miles = stravaWorkout.distance.getMiles()
        val milesString = "$miles mi"

        val milesPerHour = (stravaWorkout.average_speed * 2.237).round(2)

        stravaWorkout.formattedDate = getDateTimeDisplayString(date, time)
        stravaWorkout.duration = movingTime
        stravaWorkout.miles = milesString

        return stravaWorkout
    }
}