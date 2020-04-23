package com.bignerdranch.android.task04.data.entity

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromColor(value: Date): Long = value.time

    @TypeConverter
    fun toColor(value: Long): Date = Date(value)
}