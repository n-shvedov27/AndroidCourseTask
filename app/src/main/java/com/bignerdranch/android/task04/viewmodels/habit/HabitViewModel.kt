package com.bignerdranch.android.task04.viewmodels.habit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.db.entity.Habit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HabitViewModel(
    private val habitId: Long?,
    application: Application
) : AndroidViewModel(application), CoroutineScope {

    companion object{
        const val TAG = "HabitViewModel"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + exceptionHandler

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, throwable.message ?: throwable.toString())
    }

    private val habitRepository: HabitRepository = HabitRepository(application)

    private var mutableHabit: MutableLiveData<Habit>  = MutableLiveData()

    val habit: LiveData<Habit?>

    init {
        habit = habitId?.let {
            habitRepository.getById(it)
        } ?: run {
            mutableHabit.value = Habit()
            return@run mutableHabit
        }
    }

    public fun saveHabit() = launch {
        if (habit.value!!.id == null) {
            habitRepository.createHabit(habit.value!!)
        } else {
            habitRepository.updateHabit(habit.value!!)
        }
    }
}