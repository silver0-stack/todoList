package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myapplication.databgase.tod_TodoDatabase
import com.example.myapplication.dto.tod_Todo

private const val DATABASE_NAME = "todo-database.db"

class tod_TodoRepository private constructor(context: Context) {

    //database build
    private val database: tod_TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        tod_TodoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val todoDao = database.todoDao()

    fun list(): LiveData<MutableList<tod_Todo>> = todoDao.list()

    fun getTodo(id: Long): tod_Todo = todoDao.selectOne(id)

    fun insert(dto: tod_Todo) = todoDao.insert(dto)

    suspend fun update(dto: tod_Todo) = todoDao.update(dto)

    fun delete(dto: tod_Todo) = todoDao.delete(dto)

    //single-ton
    //클래스가 생성될 때 메모리에 적재되면서 동시에 생성하는 객체로, 데이터베이스 생성 및 초기화를 담당
    companion object {
        private var INSTANCE: tod_TodoRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = tod_TodoRepository(context)
            }
        }
        fun get():tod_TodoRepository{
            return INSTANCE?:
            throw IllegalStateException("Today TodoRepository must be initialized")
        }
    }


}