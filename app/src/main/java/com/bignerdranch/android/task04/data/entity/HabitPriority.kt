package com.bignerdranch.android.task04.data.entity

import androidx.room.TypeConverter


enum class HabitPriority(value: Int) {
    LOW(1),
    MIDDLE(2),
    HIGH(3)
}

class HabitPriorityConverter {
    @TypeConverter
    fun fromPriority(priority: HabitPriority): Int {
        return priority.ordinal
    }

    @TypeConverter
    fun toPriority(ordinal: Int): HabitPriority{
        return HabitPriority.values().first{it.ordinal == ordinal}
    }
}