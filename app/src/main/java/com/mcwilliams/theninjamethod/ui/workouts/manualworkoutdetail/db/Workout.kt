package com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db

import androidx.room.*
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Exercise

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "workout_name") val workoutName: String,
    @ColumnInfo(name = "workout_date") val workoutDate: String,
    @TypeConverters(ExerciseTypeConverter::class)
    val exercises: List<Exercise>
)