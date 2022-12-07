package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myapplication.databgase.som_TodoDatabase
import com.example.myapplication.dto.som_Todo

private const val DATABASE_NAME = "todo-database.db"

class som_TodoRepository private constructor(context: Context) {

    //database build
    private val database: som_TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        som_TodoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val todoDao = database.todoDao()

    fun list(): LiveData<MutableList<som_Todo>> = todoDao.list()

    fun getTodo(id: Long): som_Todo = todoDao.selectOne(id)

    fun insert(dto: som_Todo) = todoDao.insert(dto)

    suspend fun update(dto: som_Todo) = todoDao.update(dto)

    fun delete(dto: som_Todo) = todoDao.delete(dto)

    //single-ton
    //클래스가 생성될 때 메모리에 적재되면서 동시에 생성하는 객체로, 데이터베이스 생성 및 초기화를 담당
    companion object {
        private var INSTANCE: som_TodoRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = som_TodoRepository(context)
            }
        }
        fun get():som_TodoRepository{
            return INSTANCE?:
            throw IllegalStateException("Someday TodoRepository must be initialized")
        }
    }


}