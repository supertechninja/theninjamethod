package com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Exercise
import java.io.Serializable

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "workout_name") val workoutName: String,
    @ColumnInfo(name = "workout_date") val workoutDate: String,
    @TypeConverters(ExerciseTypeConverter::class)
    var exercises: MutableList<Exercise>? = mutableListOf()
) : Serializable {
    constructor(id: Int, workoutName: String, workoutDate: String) : this(
        id,
        workoutName,
        workoutDate,
        null
    )
}