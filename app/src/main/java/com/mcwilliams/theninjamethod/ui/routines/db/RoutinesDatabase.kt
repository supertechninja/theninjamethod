package com.mcwilliams.theninjamethod.ui.routines.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.ExerciseTypeConverter
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout

@Database(entities = arrayOf(Workout::class), version = 1)
@TypeConverters(ExerciseTypeConverter::class)
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
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}