package com.bignerdranch.android.task04.data.db.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
class Habit {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    var name: String = "New habit"

    var description: String = "description"

    @TypeConverters(HabitPriorityConverter::class)
    var priority: HabitPriority = HabitPriority.LOW

    @TypeConverters(HabitTypeConverter::class)
    var type: HabitType = HabitType.Good

    var quantity: Int = 0


    var periodicity: Int = 0

    @TypeConverters(HabitColorConverter::class)
    var color: HabitColor = HabitColor.RED
}