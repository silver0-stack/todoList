package com.example.myapplication.databgase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.dao.som_TodoDao
import com.example.myapplication.dto.som_Todo

@Database(entities = [som_Todo::class], version = 1)
abstract class som_TodoDatabase:RoomDatabase() {
    abstract fun todoDao(): som_TodoDao
}