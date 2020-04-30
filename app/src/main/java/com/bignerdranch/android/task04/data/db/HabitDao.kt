package com.bignerdranch.android.task04.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitType
import com.bignerdranch.android.task04.data.entity.HabitTypeConverter

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit WHERE type = :habitType AND title LIKE :prefix")
    @TypeConverters(HabitTypeConverter::class)
    fun getAllByType(habitType: HabitType, prefix: String): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE type = :habitType AND title LIKE :prefix ORDER BY priority ASC")
    @TypeConverters(HabitTypeConverter::class)
    fun getAllByTypeSortByAscPriority(habitType: HabitType, prefix: String): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE type = :habitType AND title LIKE :prefix ORDER BY priority DESC")
    @TypeConverters(HabitTypeConverter::class)
    fun getAllByTypeSortByDescPriority(habitType: HabitType, prefix: String): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE uid = :uid")
    fun getById(uid: String): LiveData<Habit?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habit: Habit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habits: List<Habit>)

    @Delete
    fun delete(habit: Habit)
}