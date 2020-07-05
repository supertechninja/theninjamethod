package com.mcwilliams.theninjamethod.ui.exercises.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class ExerciseHistoryTypeConverter {
    companion object {
        var gson = Gson()

        @JvmStatic
        @TypeConverter
        fun stringToExerciseHistory(data: String?): List<ExerciseHistory> {
            if (data == null) {
                return Collections.emptyList()
            }
            val listType: Type =
                object : TypeToken<List<ExerciseHistory?>?>() {}.type
            return gson.fromJson(data, listType)
        }

        @JvmStatic
        @TypeConverter
        fun exerciseHistoryToString(exercises: List<ExerciseHistory?>?): String {
            return gson.toJson(exercises)
        }
    }
}