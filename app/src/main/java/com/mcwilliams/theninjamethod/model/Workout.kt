package com.mcwilliams.theninjamethod.model

import com.google.gson.annotations.SerializedName

data class Workout(
    val date: String,
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