package com.bignerdranch.android.task04.data.db.entity

import androidx.room.TypeConverter


enum class HabitType {
    Bad,
    Good
}

class HabitTypeConverter {
    @TypeConverter
    fun fromType(value: HabitType): String = value.name

    @TypeConverter
    fun toType(value: String) = enumValueOf<HabitType>(value)
}