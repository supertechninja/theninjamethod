package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import android.content.Context
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.WorkoutType
import com.mcwilliams.theninjamethod.utils.extensions.*
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StravaWorkoutRepository @Inject constructor(
    val context: Context,
    private val athleteApi: AthleteApi
) {

    //Cache in memory the strava workouts
    private lateinit var listOfStravaWorkouts: List<ActivitesItem>

    fun getStravaActivities(): Observable<List<Workout>> =
        athleteApi.getAthleteActivities().map { mapStravaWorkouts(it) }

    //Return Detail Strava Activity to render the activity detail
    fun getStravaItemDetail(id: Number): ActivitesItem = listOfStravaWorkouts.find { it.id == id }!!

    //Return a list of strava workout summaries, a subset of the detail data needed to show the ui
    private fun mapStravaWorkouts(stravaWorkouts: List<ActivitesItem>): List<Workout> {
        val workoutList = mutableListOf<Workout>()
        stravaWorkouts.forEach {

            val date = it.start_date.getDate()
            val time = it.start_date.getTime()

            val movingTime = "${it.moving_time / 60}m ${it.moving_time % 60}s"
            val miles = it.distance.getMiles()
            val milesString = "$miles mi"

            val milesPerHour = (it.average_speed * 2.237).round(2)

            it.formattedDate = getDateTimeDisplayString(date, time)
            it.duration = movingTime
            it.miles = milesString

            val workoutItem =
                Workout(
                    date,
                    time.get12HrTime(),
                    it.name,
                    WorkoutType.STRAVA,
                    milesString,
                    "$movingTime Pace: $milesPerHour mph",
                    it.id
                )

            workoutList.add(workoutItem)
        }
        listOfStravaWorkouts = stravaWorkouts
        return workoutList
    }

}