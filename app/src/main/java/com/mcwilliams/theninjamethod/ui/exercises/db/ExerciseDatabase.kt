package com.mcwilliams.theninjamethod.ui.exercises.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Exercise::class), version = 2)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    companion object {

        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getDatabase(context: Context): ExerciseDatabase? {
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
            return INSTANCE
        }
    }
}