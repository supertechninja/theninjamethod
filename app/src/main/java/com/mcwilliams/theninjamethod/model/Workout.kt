package com.mcwilliams.theninjamethod.model

import java.util.*

data class Workout(
    val date: String,
    val exerciseName: String,
    val set: String,
    val reps: Int,
    val weight: Int,
    val notes: String
)

data class WorkoutList(
    val workouts: List<Workout>
)