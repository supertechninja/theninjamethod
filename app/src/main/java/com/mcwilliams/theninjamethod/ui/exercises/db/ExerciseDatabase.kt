package com.mcwilliams.theninjamethod.ui.exercises.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseHistoryTypeConverter

@Database(entities = arrayOf(Exercise::class), version = 3)
@TypeConverters(ExerciseHistoryTypeConverter::class)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    companion object {

        private var INSTANCE: ExerciseDatabase? = null

        @JvmStatic
        @Synchronized
        fun getDatabase(context: Context): ExerciseDatabase {
            if (INSTANCE == null) {
                synchronized(ExerciseDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ExerciseDatabase::class.java, "exercise_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}