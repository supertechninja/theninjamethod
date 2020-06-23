package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.WorkoutType
import io.reactivex.Observable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
    fun getStravaItemDetail(id: Number): LiveData<ActivitesItem> {
        val activitiesItem = listOfStravaWorkouts.find { it.id == id }
        return MutableLiveData(activitiesItem)
    }

    //Return a list of strava workout summaries, a subset of the detail data needed to show the ui
    private fun mapStravaWorkouts(stravaWorkouts: List<ActivitesItem>): List<Workout> {
        val workoutList = mutableListOf<Workout>()
        listOfStravaWorkouts = stravaWorkouts
        stravaWorkouts.forEach {
            val dtf = DateTimeFormatter.ISO_DATE_TIME
            val zdt: ZonedDateTime =
                ZonedDateTime.parse(it.start_date_local, dtf)
            val localDateTime = zdt.toLocalDateTime()
            val date = localDateTime.toLocalDate()
            val time = localDateTime.toLocalTime()

            val movingTime = "${it.moving_time / 60}m ${it.moving_time % 60}s"
            val miles = getMiles(it.distance).round(2)
            val milesString = "$miles mi"

            val milesPerHour = (it.average_speed * 2.237).round(2)

            val workoutItem =
                Workout(
                    date,
                    "${time.hour}:${time.minute}",
                    it.name,
                    WorkoutType.STRAVA,
                    milesString,
                    "$movingTime Pace: $milesPerHour mph",
                    it.id
                )

            workoutList.add(workoutItem)
        }
        return workoutList
    }

    private fun getMiles(meters: Float): Double {
        return meters * 0.000621371192;
    }

    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

}