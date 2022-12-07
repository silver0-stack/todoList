package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.dto.tod_Todo

@Dao
interface tod_TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dto: tod_Todo)

    @Query("select * from tod_todoTable")
    fun list():LiveData<MutableList<tod_Todo>>

    @Query("select * from tod_todoTable where id = (:id)")
    fun selectOne(id:Long):tod_Todo

    @Update
    suspend fun update(dto:tod_Todo)

    @Delete
    fun delete(dto:tod_Todo)
}