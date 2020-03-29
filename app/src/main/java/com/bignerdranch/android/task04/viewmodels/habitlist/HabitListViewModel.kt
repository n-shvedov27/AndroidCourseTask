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

    public var sortType = SortType.None
    set(value) {
        field = value
        load()
    }

    init {
        load()
    }

    private fun load() {
        HabitType.values().forEach {habitType ->
            run {
                if (mutableHabitsByTypeLiveData[habitType] == null) {
                    mutableHabitsByTypeLiveData[habitType] = MutableLiveData()
                }

                mutableHabitsByTypeLiveData[habitType]?.value =
                    when (sortType) {
                        SortType.None -> HabitRepository.habitList.filter { habit -> filter(habit, habitType) }
                        SortType.Ascending -> HabitRepository.habitList.filter { habit -> filter(habit, habitType) }.sortedBy { it.priority }
                        SortType.Descending -> HabitRepository.habitList.filter { habit -> filter(habit, habitType) }.sortedByDescending { it.priority }
                    }

            }
        }
    }

    public fun updateHabits(habitType: HabitType){
        mutableHabitsByTypeLiveData[habitType]?.value = HabitRepository.habitList.filter { filter(it, habitType) }
    }

    private fun filter(habit: Habit, habitType: HabitType) = habit.type == habitType && habit.name.startsWith(filterPrefix, true)
}

enum class SortType{
    Ascending, Descending, None
}