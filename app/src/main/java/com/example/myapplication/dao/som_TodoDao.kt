package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.dto.som_Todo

@Dao
interface som_TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dto: som_Todo)

    @Query("select * from som_todoTable")
    fun list():LiveData<MutableList<som_Todo>>

    @Query("select * from som_todoTable where id = (:id)")
    fun selectOne(id:Long):som_Todo

    @Update
    suspend fun update(dto:som_Todo)

    @Delete
    fun delete(dto:som_Todo)
}