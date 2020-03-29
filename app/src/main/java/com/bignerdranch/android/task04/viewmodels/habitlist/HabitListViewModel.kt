package com.bignerdranch.android.task04.viewmodels.habitlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitType
import java.util.*

class HabitListViewModel: ViewModel() {
    private val mutableHabitsByTypeLiveData: MutableMap<HabitType, MutableLiveData<List<Habit>>> = EnumMap(HabitType::class.java)
    val habitsByType: Map<HabitType, LiveData<List<Habit>>> = mutableHabitsByTypeLiveData

    public var filterPrefix = ""
    set(value)  {
        field = value
        load()
    }

    init {
        load()
    }

    private fun load() {
        HabitType.values().forEach {
            if (mutableHabitsByTypeLiveData[it] == null) {
                mutableHabitsByTypeLiveData[it] = MutableLiveData()
            }
            mutableHabitsByTypeLiveData[it]?.value = HabitRepository.habitList.filter {
                    habit -> habit.type == it && habit.name.startsWith(filterPrefix)
            }
        }
    }

    public fun updateHabits(habitType: HabitType){
        mutableHabitsByTypeLiveData[habitType]?.value = HabitRepository.habitList.filter {
                habit -> habit.type == habitType && habit.name.startsWith(filterPrefix)
        }
    }
}