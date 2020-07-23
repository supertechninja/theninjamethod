package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout")
    fun getAll(): Flowable<List<Workout>>

    @Query("SELECT * FROM workout WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Workout>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Insert
    suspend fun insertAll(vararg users: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("DELETE FROM workout")
    suspend fun nukeTable()
}