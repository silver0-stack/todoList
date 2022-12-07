package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.dto.tom_Todo

@Dao
interface tom_TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dto: tom_Todo)

    @Query("select * from tom_todoTable")
    fun list():LiveData<MutableList<tom_Todo>>

    @Query("select * from tom_todoTable where id = (:id)")
    fun selectOne(id:Long):tom_Todo

    @Update
    suspend fun update(dto:tom_Todo)

    @Delete
    fun delete(dto:tom_Todo)
}