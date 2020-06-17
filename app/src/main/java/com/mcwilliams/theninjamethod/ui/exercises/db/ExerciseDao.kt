package com.mcwilliams.theninjamethod.ui.exercises.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    suspend fun getAll(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Exercise>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    suspend fun insertAll(vararg users: Exercise)

    @Delete
    suspend fun delete(user: Exercise)

    @Query("DELETE FROM exercise")
    suspend fun nukeTable()
}