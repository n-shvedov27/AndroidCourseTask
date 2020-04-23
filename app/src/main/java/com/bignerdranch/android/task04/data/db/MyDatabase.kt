package com.bignerdranch.android.task04.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bignerdranch.android.task04.data.entity.Habit


@Database(entities = [Habit::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        private var instance: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            instance?.let {
                return it
            } ?: run {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "database"
                )
                    .build()
                return instance!!
            }
        }
    }
}