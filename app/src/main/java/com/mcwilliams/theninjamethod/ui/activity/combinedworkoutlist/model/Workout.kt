package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model

import java.time.LocalDate

data class Workout(
    val date: LocalDate,
    val time: String,
    val exerciseName: String,
    var workoutType: WorkoutType,
    var stravaDistance: String,
    var workoutCaloriesBurned: String,
    var stravaTime: String,
    var id: Number
)

data class WorkoutList(
    val workouts: List<Workout>
)

enum class WorkoutType {
    LIFTING, STRAVA
}