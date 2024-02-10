package com.example.task1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDao {

    @Insert
    fun insertToDo(ToDoEntity: ToDoEntity)

    @Query("Select * From ToDoEntity")
    fun getToDoEntities(): List<ToDoEntity>

    @Update
    fun updateToDoEntity(ToDoEntity: ToDoEntity)

    @Delete
    fun deleteToDoEntity(ToDoEntity: ToDoEntity)
}