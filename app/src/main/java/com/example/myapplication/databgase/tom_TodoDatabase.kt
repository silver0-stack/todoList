package com.example.myapplication.databgase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.dao.tom_TodoDao
import com.example.myapplication.dto.tom_Todo

@Database(entities = [tom_Todo::class], version = 1)
abstract class tom_TodoDatabase:RoomDatabase() {
    abstract fun todoDao(): tom_TodoDao
}