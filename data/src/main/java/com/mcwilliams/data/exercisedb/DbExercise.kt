package com.mcwilliams.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mcwilliams.data.exercisedb.model.ExerciseType

@Entity
data class DbExercise(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "exercise_name") val exerciseName: String,
    @ColumnInfo(name = "exercise_type") val exerciseType: String?,
    @ColumnInfo(name = "exercise_body_part") val exerciseBodyPart: String?,
    @ColumnInfo(name = "definedExerciseType") var definedExerciseType: ExerciseType?
)

data class Data(val exercises: List<DbExercise>)