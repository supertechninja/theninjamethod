package com.mcwilliams.theninjamethod.ui.workouts.repo

import android.content.Context
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.ui.workouts.model.Workout
import com.mcwilliams.theninjamethod.ui.workouts.model.WorkoutType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StravaWorkoutRepository @Inject constructor(val context: Context, private val athleteApi: AthleteApi) :
    IWorkoutRepo {

    //Cache in memory the strava workouts
    private var listOfStravaWorkouts = listOf<ActivitesItem>()


    override suspend fun getStravaActivities(): Result<List<Workout>> {
        if (listOfStravaWorkouts.isNotEmpty()) {
            return Result.Success(mapStravaWorkouts())
        } else {
            withContext(Dispatchers.IO) {
                listOfStravaWorkouts = athleteApi.getAthleteActivities()
            }

        }
        return Result.Success(mapStravaWorkouts())
    }

    //Return Detail Strava Activity to render the activity detail
    fun getStravaItemDetail(id: Number) : ActivitesItem? = listOfStravaWorkouts.find { it.id == id }

    //Return a list of strava workout summaries, a subset of the detail data needed to show the ui
    private fun mapStravaWorkouts() : List<Workout> {
        val workoutList = mutableListOf<Workout>()
        listOfStravaWorkouts.forEach {
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