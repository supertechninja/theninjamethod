package com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout")
    suspend fun getAll(): List<Workout>

    @Query("SELECT * FROM workout WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Workout>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    suspend fun insertAll(vararg users: Workout)

    @Delete
    suspend fun delete(user: Workout)

    @Query("DELETE FROM workout")
    suspend fun nukeTable()
}