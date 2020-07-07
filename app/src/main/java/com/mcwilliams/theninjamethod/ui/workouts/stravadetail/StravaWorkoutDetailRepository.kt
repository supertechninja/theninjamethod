package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.utils.extensions.*
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StravaWorkoutDetailRepository @Inject constructor(
    private val athleteApi: AthleteApi
) {
    //Return Detail Strava Activity to render the activity detail
    fun getStravaItemDetail(id: Number): Observable<StravaActivityDetail> {
        return athleteApi.getActivityDetail(id, true).map { mapStravaWorkouts(it) }
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