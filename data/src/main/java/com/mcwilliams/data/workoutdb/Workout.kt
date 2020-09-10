package com.mcwilliams.data.workoutdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mcwilliams.data.exercisedb.model.WorkoutExercise
import java.io.Serializable

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "workout_name") var workoutName: String,
    @ColumnInfo(name = "workout_date") val workoutDate: String,
    @ColumnInfo(name = "workout_difficulty") val workoutDifficulty: String?,
    @ColumnInfo(name = "workout_duration") var workoutDuration: String?,
    @ColumnInfo(name = "workout_total_weight") var workoutTotalWeight: String?,
    @ColumnInfo(name = "workout_calories_burned") var caloriesBurned: String?,
    @TypeConverters(ExerciseTypeConverter::class)
    var exercises: MutableList<WorkoutExercise> = mutableListOf()
) : Serializable {
    constructor(id: Int, workoutName: String, workoutDate: String) : this(
        id,
        workoutName,
        workoutDate,
        null,
        null,
        null,
        null,
        mutableListOf()
    )
}