package com.mcwilliams.theninjamethod.ui.workouts.ui.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Workout(
    val date: LocalDate,
    val time: String,
    @SerializedName("exerciseName")
    val workoutName: String,
    var workoutType: WorkoutType,
    var stravaDistance: String,
    var stravaTime: String
)

data class WorkoutList(
    val workouts: List<Workout>
)

enum class WorkoutType {
    LIFTING, STRAVA
}