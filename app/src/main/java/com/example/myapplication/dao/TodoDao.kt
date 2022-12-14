package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.dto.Todo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dto: Todo)

    @Query("select * from tod_todoTable")
    fun list():LiveData<MutableList<Todo>>

    @Query("select * from tod_todoTable where id = (:id)")
    fun selectOne(id:Long):Todo

    @Update
    suspend fun update(dto:Todo)

    @Delete
    fun delete(dto:Todo)
}