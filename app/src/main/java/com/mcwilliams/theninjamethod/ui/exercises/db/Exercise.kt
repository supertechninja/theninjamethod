package com.mcwilliams.theninjamethod.ui.exercises.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "exercise_name") val exerciseName: String,
    @ColumnInfo(name = "exercise_type") val exerciseType: String?,
    @ColumnInfo(name = "exercise_body_part") val exerciseBodyPart: String?,
    @ColumnInfo(name = "definedExerciseType") var definedExerciseType: ExerciseType?
)

data class Data(val exercises: List<Exercise>)