package com.bignerdranch.android.task04.viewmodels.habit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.db.entity.Habit

class HabitViewModel(private val habitId: Long?, application: Application) : AndroidViewModel(application) {
    private val habitRepository: HabitRepository = HabitRepository(application)
    private val mutableHabit: MutableLiveData<Habit> = MutableLiveData()

    val habit: LiveData<Habit> = mutableHabit

    init {
        load()
    }

    private fun load() {
        habitId?.let {
            mutableHabit.value = habitRepository.getById(it)
        } ?: run {
            val newHabitId = habitRepository.createHabit(Habit())
            mutableHabit.value = habitRepository.getById(newHabitId)
        }
    }

    public fun saveHabit(habit: Habit) {
        habitRepository.updateHabit(habit)
    }
}