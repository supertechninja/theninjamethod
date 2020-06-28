package com.mcwilliams.theninjamethod.ui.exercises.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    fun getAllFlow(): Flowable<List<Exercise>>

    @Query("SELECT * FROM exercise WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Exercise>

    @Insert
    suspend fun insertAll(vararg users: Exercise)

    @Delete
    suspend fun delete(user: Exercise)

    @Query("DELETE FROM exercise")
    suspend fun nukeTable()
}