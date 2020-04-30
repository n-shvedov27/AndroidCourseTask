package com.bignerdranch.android.task04.viewmodels.habitlist

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitType
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext

class HabitListViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val habitRepository: HabitRepository = HabitRepository(application)

    private val habitSources: MutableMap<HabitType, LiveData<List<Habit>>> = EnumMap(HabitType::class.java)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, throwable.message ?: throwable.toString())
        }

    val habitMediatorLiveData: Map<HabitType, MediatorLiveData<List<Habit>>> = HabitType.values().map { it to MediatorLiveData<List<Habit>>() }.toMap()

    private var filter = "%"

    private val token = "f59c1469-32ec-4d4b-b00d-058fc294a6b4"

    private var sortType = SortType.None

    init {
        HabitType.values().forEach {
            loadHabitSource(it)
        }

        launch(Dispatchers.IO) {
            var habitsResponse : Response<List<Habit>>? = null

            while (habitsResponse?.isSuccessful != true) {

                try {
                    habitsResponse = habitRepository.loadNew(token)
                } catch (e: IOException) {
                    Log.e(TAG, e.message ?: e.toString())
                }
                delay(1000)

                if (habitsResponse?.code() == 401) {
                    Toast.makeText(getApplication(), "Ошибка Авторизации", Toast.LENGTH_LONG).show()
                    break
                }
            }

            habitsResponse?.body()?.let { habitRepository.putHabits(it) }
        }
    }

    private fun loadHabitSource(habitType: HabitType) {
        val habitSource: LiveData<List<Habit>> = habitRepository.getAll(habitType, filter, sortType)

        habitSources[habitType] = habitSource

        habitMediatorLiveData[habitType]?.addSource(habitSource) {
            habitMediatorLiveData[habitType]?.value = it
        }
    }

    private fun refresh() {
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

    companion object{
        const val TAG = "HabitListViewModel"
    }
}

enum class SortType{
    Ascending, Descending, None
}