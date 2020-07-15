package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate

data class Workout(
    val date: LocalDate,
    val time: String,
    @SerializedName("exerciseName")
    val workoutName: String,
    var workoutType: WorkoutType,
    var stravaDistance: String,
    var stravaTime: String,
    var id: Number
) : Serializable

data class WorkoutList(
    val workouts: List<Workout>
)

enum class WorkoutType {
    LIFTING, STRAVA
}