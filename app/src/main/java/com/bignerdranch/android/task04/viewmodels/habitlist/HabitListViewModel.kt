package com.bignerdranch.android.task04.viewmodels.habitlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.db.entity.Habit
import com.bignerdranch.android.task04.data.db.entity.HabitType
import java.util.*

class HabitListViewModel(application: Application) : AndroidViewModel(application) {
    private val habitRepository: HabitRepository = HabitRepository(application)
    private val mutableHabitsByTypeLiveData: MutableMap<HabitType, MutableLiveData<List<Habit>>> = EnumMap(HabitType::class.java)
    val habitsByType: Map<HabitType, LiveData<List<Habit>>> = mutableHabitsByTypeLiveData



    public var filterPrefix = "%"
    set(value)  {
        field = "$value%"
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

                val a = habitRepository.getAll(habitType, filterPrefix, sortType)
                mutableHabitsByTypeLiveData[habitType]?.value =  a
            }
        }
    }

    public fun updateHabits(habitType: HabitType){
        mutableHabitsByTypeLiveData[habitType]?.value = habitRepository.getAll(habitType, filterPrefix, sortType)
    }
}

enum class SortType{
    Ascending, Descending, None
}