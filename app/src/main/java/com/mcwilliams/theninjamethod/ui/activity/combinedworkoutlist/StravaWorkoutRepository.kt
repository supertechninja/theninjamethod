package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import android.content.Context
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.data.workoutdb.WorkoutType
import com.mcwilliams.theninjamethod.strava.api.ActivitiesApi
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.utils.extensions.*
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StravaWorkoutRepository @Inject constructor(
    val context: Context,
    private val activitiesApi: ActivitiesApi
) {

    //Cache in memory the strava workouts
    private lateinit var listOfStravaWorkouts: List<ActivitesItem>

    fun getStravaActivities(): Observable<List<SimpleWorkout>> =
        activitiesApi.getAthleteActivities().map { mapStravaWorkouts(it) }


    //Return a list of strava workout summaries, a subset of the detail data needed to show the ui
    private fun mapStravaWorkouts(stravaWorkouts: List<ActivitesItem>): List<SimpleWorkout> {
        val workoutList = mutableListOf<SimpleWorkout>()
        stravaWorkouts.forEach {

            val date = it.start_date_local.getDate()
            val time = it.start_date_local.getTime()

            val movingTime = it.moving_time.getTimeString()
            val miles = it.distance.getMiles()

            val milesPerHour = (it.average_speed * 2.237).round(2)

            val pace = it.moving_time.getPaceFromMovingTime(it.distance)
            val milesString = "${miles}mi"

            //Set non-api app properties
            it.formattedDate = getDateTimeDisplayString(date, time)
            it.duration = movingTime
            it.miles = milesString

            var calories = ""
            it.calories?.let { stravaCalories ->
                calories = stravaCalories.toString()
            }

            val workoutItem =
                SimpleWorkout(
                    date,
                    time.toString(),
                    it.name,
                    WorkoutType.STRAVA,
                    milesString,
                    calories,
                    movingTime,
                    it.id
                )

            workoutList.add(workoutItem)
        }
        listOfStravaWorkouts = stravaWorkouts
        return workoutList
    }

}