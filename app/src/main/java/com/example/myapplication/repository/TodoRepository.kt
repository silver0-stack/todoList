package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myapplication.dto.databgase.tod_TodoDatabase
import com.example.myapplication.dto.Todo

private const val DATABASE_NAME = "todo-database.db"

class TodoRepository private constructor(context: Context) {

    //database build
    private val database: tod_TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        tod_TodoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val todoDao = database.todoDao()

    fun list(): LiveData<MutableList<Todo>> = todoDao.list()

    fun getTodo(id: Long): Todo = todoDao.selectOne(id)

    fun insert(dto: Todo) = todoDao.insert(dto)

    suspend fun update(dto: Todo) = todoDao.update(dto)

    fun delete(dto: Todo) = todoDao.delete(dto)

    //single-ton
    //클래스가 생성될 때 메모리에 적재되면서 동시에 생성하는 객체로, 데이터베이스 생성 및 초기화를 담당
    companion object {
        private var INSTANCE: TodoRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TodoRepository(context)
            }
        }
        fun get():TodoRepository{
            return INSTANCE?:
            throw IllegalStateException("TodoRepository must be initialized")
        }
    }


}