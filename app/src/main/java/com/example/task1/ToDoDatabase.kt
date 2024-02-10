package com.example.task1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDoEntity::class], version = 1)
abstract class ToDoDatabase : RoomDatabase(){

    abstract fun todoDao(): ToDoDao

    companion object{
        var toDoDatabase: ToDoDatabase? = null

        @Synchronized
        fun getDatabaseInstance(context: Context): ToDoDatabase{
            if (toDoDatabase == null){
                toDoDatabase = Room.databaseBuilder(context,
                    ToDoDatabase::class.java,
                    context.resources.getString(R.string.app_name))
                    .allowMainThreadQueries()
                    .build()
            }
            return toDoDatabase!!
        }

    }
}