package com.example.myapplication.dto.databgase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.dao.TodoDao
import com.example.myapplication.dto.Todo

@Database(entities = [Todo::class], version = 1)
abstract class tod_TodoDatabase:RoomDatabase() {
    abstract fun todoDao():TodoDao
}