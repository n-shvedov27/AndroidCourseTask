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
            mutableHabit.value = Habit()
        }
    }

    public fun saveHabit() {
        if (habit.value!!.id == null) {
            habitRepository.createHabit(habit.value!!)
        } else {
            habitRepository.updateHabit(habit.value!!)
        }
    }
}