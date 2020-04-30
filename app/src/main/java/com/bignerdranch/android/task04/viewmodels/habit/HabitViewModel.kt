package com.bignerdranch.android.task04.viewmodels.habit

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.network.response.HabitPushResponse
import com.bignerdranch.android.task04.viewmodels.habitlist.HabitListViewModel
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
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

        var pushResponse : Response<HabitPushResponse>? = null

        while (pushResponse?.isSuccessful != true) {

            try {
                pushResponse = habitRepository.push(habit.value!!, token)
            } catch (e: IOException) {
                Log.e(HabitListViewModel.TAG, e.message ?: e.toString())
            }

            delay(1000)

            if (pushResponse?.code() == 401) {
                Toast.makeText(getApplication(), "Ошибка Авторизации", Toast.LENGTH_LONG).show()
                break
            }
        }

        pushResponse?.body()?.let {
            val uid = UUID.fromString(it.uid)
            habit.value!!.uid = uid
            habitRepository.putHabit(habit.value!!)
        }
    }

    public fun deleteHabit() = launch(Dispatchers.IO) {

        var deleteResponse : Response<ResponseBody>? = null

        while (deleteResponse?.isSuccessful != true) {

            try {
                deleteResponse = habitRepository.deleteFromServer(habit.value!!, token)
            } catch (e: IOException) {
                Log.e(HabitListViewModel.TAG, e.message ?: e.toString())
            }

            delay(1000)

            if (deleteResponse?.code() == 401) {
                Toast.makeText(getApplication(), "Ошибка Авторизации", Toast.LENGTH_LONG).show()
                break
            }
        }

        habitRepository.deleteFromDb(habit.value!!)
    }
}