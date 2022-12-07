package com.example.myapplication.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "som_todoTable")
class som_Todo(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "todo") val todo: String,
    @ColumnInfo(name = "isChecked") val isChecked: Boolean
): Serializable {

}