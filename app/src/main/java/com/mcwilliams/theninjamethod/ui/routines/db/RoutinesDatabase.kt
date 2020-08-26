package com.mcwilliams.theninjamethod.ui.routines.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(com.mcwilliams.data.workoutdb.Workout::class), version = 5)
@TypeConverters(com.mcwilliams.data.workoutdb.ExerciseTypeConverter::class)
abstract class RoutinesDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {

        @Volatile
        private var INSTANCE: RoutinesDatabase? = null

        fun getDatabase(context: Context): RoutinesDatabase? {
            if (INSTANCE == null) {
                synchronized(RoutinesDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            RoutinesDatabase::class.java, "routines_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}