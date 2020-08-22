package com.mcwilliams.theninjamethod.ui.routines.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface RoutineDao {
    @Query("SELECT * FROM workout")
    fun getAll(): Flowable<List<com.mcwilliams.data.workoutdb.Workout>>

    @Query("SELECT * FROM workout WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<com.mcwilliams.data.workoutdb.Workout>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    suspend fun insertAll(vararg users: com.mcwilliams.data.workoutdb.Workout)

    @Delete
    suspend fun delete(workout: com.mcwilliams.data.workoutdb.Workout)

    @Query("DELETE FROM workout")
    suspend fun nukeTable()
}