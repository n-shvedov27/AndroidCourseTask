package com.bignerdranch.android.task04.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.bignerdranch.android.task04.data.db.HabitDao
import com.bignerdranch.android.task04.data.db.MyDatabase
import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitType
import com.bignerdranch.android.task04.data.entity.HabitTypeAdapter
import com.bignerdranch.android.task04.data.network.HabitApi
import com.bignerdranch.android.task04.util.Constants
import com.bignerdranch.android.task04.viewmodels.habitlist.SortType
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class HabitRepository(application: Application) {
    private val habitDao: HabitDao = MyDatabase.getDatabase(application).habitDao()

    private var habitApi: HabitApi

    init {

        val gson = GsonBuilder()
            .registerTypeAdapter(Habit::class.java, HabitTypeAdapter())
            .create()

        val okHttpClient= OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        habitApi = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(HabitApi::class.java)
    }

    fun getAll(habitType: HabitType, prefix: String, sortType: SortType): LiveData<List<Habit>> =
        when (sortType) {
            SortType.None -> habitDao.getAllByType(habitType, prefix)
            SortType.Ascending -> habitDao.getAllByTypeSortByAscPriority(habitType, prefix)
            SortType.Descending -> habitDao.getAllByTypeSortByDescPriority(habitType, prefix)
        }

    fun getById(id: UUID): LiveData<Habit?> = habitDao.getById(id.toString())

    fun putHabit(habit: Habit) = habitDao.insert(habit)

    fun putHabits(habits: List<Habit>) = habitDao.insert(habits)

    suspend fun loadNew(token: String) = habitApi.getHabits(token)

    suspend fun push(habit: Habit, token: String) = habitApi.putHabit(habit, token)

    fun deleteFromDb(habit: Habit) = habitDao.delete(habit)

    suspend fun deleteFromServer(habit: Habit, token: String) = habitApi.deleteHabit(habit, token)
}