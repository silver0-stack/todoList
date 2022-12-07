package com.example.myapplication.databgase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.dao.tod_TodoDao
import com.example.myapplication.dto.tod_Todo

@Database(entities = [tod_Todo::class], version = 1)
abstract class tod_TodoDatabase:RoomDatabase() {
    abstract fun todoDao():tod_TodoDao
}