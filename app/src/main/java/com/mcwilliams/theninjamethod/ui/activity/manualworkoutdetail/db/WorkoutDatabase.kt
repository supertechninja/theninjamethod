package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Workout::class), version = 1)
@TypeConverters(ExerciseTypeConverter::class)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {

        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: Context): WorkoutDatabase? {
            if (INSTANCE == null) {
                synchronized(WorkoutDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            WorkoutDatabase::class.java, "workout_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}