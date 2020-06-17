package com.mcwilliams.theninjamethod.ui.workouts.db

import androidx.room.*
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.model.ExerciseTypeConverter

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "workout_name") val firstName: String?,
    @ColumnInfo(name = "workout_date") val lastName: String?,
    @TypeConverters(ExerciseTypeConverter::class)
    val exercises: List<Exercise>
)