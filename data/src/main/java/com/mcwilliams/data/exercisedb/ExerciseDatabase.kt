package com.mcwilliams.data.exercisedb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcwilliams.data.exercisedb.model.ExerciseHistoryTypeConverter

@Database(entities = arrayOf(DbExercise::class), version = 4)
@TypeConverters(ExerciseHistoryTypeConverter::class)
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
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}