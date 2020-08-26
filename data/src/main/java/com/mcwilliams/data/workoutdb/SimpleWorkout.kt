package com.mcwilliams.data.workoutdb

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate

data class SimpleWorkout(
    val date: LocalDate,
    val time: String,
    @SerializedName("exerciseName")
    val workoutName: String,
    var workoutType: WorkoutType,
    var stravaDistance: String,
    var workoutCaloriesBurned: String,
    var stravaTime: String,
    var id: Number
) : Serializable

data class WorkoutList(
    val workouts: List<SimpleWorkout>
)

enum class WorkoutType {
    LIFTING, STRAVA
}