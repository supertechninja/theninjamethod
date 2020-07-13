package com.mcwilliams.theninjamethod.ui.exercises.model

import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.WorkoutSet
import java.time.LocalDate

data class ExerciseHistory(val date: LocalDate, val sets: List<WorkoutSet>)