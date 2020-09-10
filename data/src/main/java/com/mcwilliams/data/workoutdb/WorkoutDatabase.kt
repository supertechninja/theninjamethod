package com.mcwilliams.data.workoutdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Workout::class), version = 7)
@TypeConverters(ExerciseTypeConverter::class)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {

        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

//        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "ALTER TABLE workout "
//                            + " ADD COLUMN workout_difficulty TEXT"
//                )
//            }
//        }

        fun getDatabase(context: Context): WorkoutDatabase? {
            if (INSTANCE == null) {
                synchronized(WorkoutDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            WorkoutDatabase::class.java, "workout_database"
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