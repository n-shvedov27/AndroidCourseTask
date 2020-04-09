package com.bignerdranch.android.task04.viewmodels.habitlist

import android.app.Application
import androidx.lifecycle.*
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.db.entity.Habit
import com.bignerdranch.android.task04.data.db.entity.HabitType
import java.util.*

class HabitListViewModel(application: Application) : AndroidViewModel(application) {
    private val habitRepository: HabitRepository = HabitRepository(application)

    private val habitSources: MutableMap<HabitType, LiveData<List<Habit>>> = EnumMap(HabitType::class.java)

    val habitMediatorLiveData: Map<HabitType, MediatorLiveData<List<Habit>>> = HabitType.values().map { it to MediatorLiveData<List<Habit>>() }.toMap()

    private var filter = "%"

    private var sortType = SortType.None

    init {
        HabitType.values().forEach {
            loadHabitSource(it)
        }
    }

    private fun loadHabitSource(habitType: HabitType) {
        val habitSource: LiveData<List<Habit>> = habitRepository.getAll(habitType, filter, sortType)

        habitSources[habitType] = habitSource

        habitMediatorLiveData[habitType]?.addSource(habitSource) {
            val a = it
            habitMediatorLiveData[habitType]?.value = it
        }
    }

    fun refresh() {
        HabitType.values().forEach {habitType ->
            run {
                val previousSource: LiveData<List<Habit>> = habitSources[habitType]!!

                habitMediatorLiveData[habitType]?.removeSource(previousSource)

                loadHabitSource(habitType)
            }
        }
    }

    fun updateFilter(filter: String) {
        this.filter = "$filter%"
        refresh()
    }

    fun updateSortType(sortType: SortType) {
        this.sortType = sortType
        refresh()
    }

//
//    private val mutableHabitsByTypeLiveData: MutableMap<HabitType, MutableLiveData<List<Habit>>> = EnumMap(HabitType::class.java)
//    val habitsByType: Map<HabitType, LiveData<List<Habit>>> = mutableHabitsByTypeLiveData
//
//
//
//    public var filterPrefix = "%"
//    set(value)  {
//        field = "$value%"
//        load()
//    }
//
//    public var sortType = SortType.None
//    set(value) {
//        field = value
//        load()
//    }
//
//    init {
//        load()
//    }
//
//    private fun load() {
//        HabitType.values().forEach {habitType ->
//            run {
//                if (mutableHabitsByTypeLiveData[habitType] == null) {
//                    mutableHabitsByTypeLiveData[habitType] = MutableLiveData()
//                }
//
//                val a = habitRepository.getAll(habitType, filterPrefix, sortType)
//                mutableHabitsByTypeLiveData[habitType]?.value =  a
//            }
//        }
//    }
//
//    public fun updateHabits(habitType: HabitType){
//        mutableHabitsByTypeLiveData[habitType]?.value = habitRepository.getAll(habitType, filterPrefix, sortType)
//    }
}

enum class SortType{
    Ascending, Descending, None
}