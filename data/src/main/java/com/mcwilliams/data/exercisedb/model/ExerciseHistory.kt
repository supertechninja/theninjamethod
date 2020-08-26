package com.mcwilliams.data.exercisedb.model

import java.time.LocalDate

data class ExerciseHistory(val date: LocalDate, val sets: List<WorkoutSet>)