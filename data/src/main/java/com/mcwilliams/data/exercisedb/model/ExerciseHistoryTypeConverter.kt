package com.mcwilliams.data.exercisedb.model

import androidx.room.TypeConverter

class ExerciseHistoryTypeConverter {
    companion object {
        @JvmStatic
        @TypeConverter
        fun toExerciseType(value: String) = enumValueOf<ExerciseType>(value)

        @JvmStatic
        @TypeConverter
        fun fromExerciseType(value: ExerciseType) = value.name
    }
}