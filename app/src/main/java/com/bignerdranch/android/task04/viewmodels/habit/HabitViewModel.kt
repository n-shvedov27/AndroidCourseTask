package com.bignerdranch.android.task04.viewmodels.habit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.entity.Habit
import java.util.*

class HabitViewModel(private val habitId: UUID?): ViewModel() {
    private val mutableHabit: MutableLiveData<Habit> = MutableLiveData()

    val habit: LiveData<Habit> = mutableHabit

    init {
        load()
    }

    private fun load() {
        if (habitId == null) {
            mutableHabit.value = Habit()
        } else {
            mutableHabit.value = HabitRepository.getHabit(habitId)
        }
    }

    public fun saveHabit(habit: Habit) {

        mutableHabit.value = habit
        HabitRepository.saveHabit(habit)
    }
}