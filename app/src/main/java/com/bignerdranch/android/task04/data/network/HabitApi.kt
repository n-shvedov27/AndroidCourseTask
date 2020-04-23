package com.bignerdranch.android.task04.data.network

import com.bignerdranch.android.task04.data.entity.Habit
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface HabitApi {

    @GET("habit")
    suspend fun getHabits(
        @Header("Authorization") token: String
    ): Response<List<Habit>>

    @PUT("habit")
    suspend fun putHabit(
        @Body habit: Habit,
        @Header("Authorization") token: String
    ): Response<ResponseBody>

    @DELETE("users/{user}/repos")
    suspend fun deleteHabit(
        @Path("user") userName: String,
        @Header("Authorization") token: String
    ): Response<ResponseBody>
}