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

}

enum class SortType{
    Ascending, Descending, None
}