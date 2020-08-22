package com.mcwilliams.data.workoutdb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mcwilliams.data.exercisedb.model.WorkoutExercise
import java.lang.reflect.Type
import java.util.*

class ExerciseTypeConverter {
    companion object {
        var gson = Gson()

        @JvmStatic
        @TypeConverter
        fun stringToExercisesList(data: String?): List<WorkoutExercise> {
            if (data == null) {
                return Collections.emptyList()
            }
            val listType: Type =
                object : TypeToken<List<WorkoutExercise?>?>() {}.type
            return gson.fromJson(data, listType)
        }

        @JvmStatic
        @TypeConverter
        fun exerciseListToString(exercises: List<WorkoutExercise?>?): String {
            return gson.toJson(exercises)
        }
    }
}