package com.bignerdranch.android.task04.viewmodels.habit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.entity.Habit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext


class HabitViewModel(
    habitId: UUID?,
    application: Application
) : AndroidViewModel(application), CoroutineScope {

    companion object{
        const val TAG = "HabitViewModel"
    }

    private val token = "f59c1469-32ec-4d4b-b00d-058fc294a6b4"

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

    public fun saveHabit() = launch(Dispatchers.IO) {
        habit.value!!.date = Date()
        val pushResponse = habitRepository.push(habit.value!!, token)

        if (pushResponse.code() == 200) {
            val s = pushResponse.body()?.string()

            val rawJson = s!!.substring(0, s.length-1)
            val jObjError = JSONObject(rawJson)

            val uid = UUID.fromString(jObjError.getString("uid"))

            habit.value!!.uid = uid
            habitRepository.putHabit(habit.value!!)
        }
    }
}