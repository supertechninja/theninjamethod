package com.mcwilliams.data.exercisedb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM dbexercise")
    fun getAllFlow(): Flowable<List<DbExercise>>

    @Query("SELECT * FROM dbexercise WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<DbExercise>

    @Insert
    suspend fun insertAll(vararg users: DbExercise)

    @Delete
    suspend fun delete(exercise: DbExercise)

    @Query("DELETE FROM dbexercise")
    suspend fun nukeTable()
}