package com.bignerdranch.android.task04.data.db.entity

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit WHERE type = :habitType AND name LIKE :prefix")
    @TypeConverters(HabitTypeConverter::class)
    fun getAllByType(habitType: HabitType, prefix: String): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE type = :habitType AND name LIKE :prefix ORDER BY priority ASC")
    @TypeConverters(HabitTypeConverter::class)
    fun getAllByTypeSortByAscPriority(habitType: HabitType, prefix: String): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE type = :habitType AND name LIKE :prefix ORDER BY priority DESC")
    @TypeConverters(HabitTypeConverter::class)
    fun getAllByTypeSortByDescPriority(habitType: HabitType, prefix: String): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE id = :id")
    fun getById(id: Long): Habit?

    @Insert
    fun insert(employee: Habit): Long

    @Update
    fun update(employee: Habit)
}