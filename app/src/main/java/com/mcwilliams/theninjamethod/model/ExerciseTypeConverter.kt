package com.mcwilliams.theninjamethod.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class ExerciseTypeConverter {
    companion object {
        var gson = Gson()
        @JvmStatic
        @TypeConverter
        fun stringToExercisesList(data: String?): List<Exercise> {
            if (data == null) {
                return Collections.emptyList()
            }
            val listType: Type =
                object : TypeToken<List<Exercise?>?>() {}.type
            return gson.fromJson(data, listType)
        }

        @JvmStatic
        @TypeConverter
        fun exerciseListToString(exercises: List<Exercise?>?): String {
            return gson.toJson(exercises)
        }
    }
}