package com.bignerdranch.android.task04.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.bignerdranch.android.task04.data.db.MyDatabase
import com.bignerdranch.android.task04.data.db.entity.Habit
import com.bignerdranch.android.task04.data.db.entity.HabitDao
import com.bignerdranch.android.task04.data.db.entity.HabitType
import com.bignerdranch.android.task04.viewmodels.habitlist.SortType


class HabitRepository(application: Application) {
    private val habitDao: HabitDao = MyDatabase.getDatabase(application).habitDao()

    fun getAll(habitType: HabitType, prefix: String, sortType: SortType): LiveData<List<Habit>> =
        when (sortType) {
            SortType.None -> habitDao.getAllByType(habitType, prefix)
            SortType.Ascending -> habitDao.getAllByTypeSortByAscPriority(habitType, prefix)
            SortType.Descending -> habitDao.getAllByTypeSortByDescPriority(habitType, prefix)
        }

    fun getById(id: Long): LiveData<Habit?> = habitDao.getById(id)

    fun createHabit(habit: Habit) = habitDao.insert(habit)

    fun updateHabit(habit: Habit) = habitDao.update(habit)
}