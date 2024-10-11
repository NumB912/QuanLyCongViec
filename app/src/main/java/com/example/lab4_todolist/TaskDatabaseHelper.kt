package com.example.lab4_todolist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class TaskDatabaseHelper(context: Context) :SQLiteOpenHelper(context,"task.db",null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

   fun addTask(task: Task){
        val db = this.writableDatabase
       val values = ContentValues().apply {
           put("name",task.name)
           put("description",task.description)
       }

       db.insert("tasks",null,values)
       db.close()
   }

    @SuppressLint("Range")
    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor = db.query("tasks", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                tasks.add(Task(id, name, description))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasks
    }

    fun updateTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", task.name)
            put("description", task.description)
        }
        db.update("tasks", values, "id = ?", arrayOf(task.ID.toString()))
        db.close()
    }

    fun deleteTask(taskId: Int) {
        val db = this.writableDatabase
        db.delete("tasks", "id = ?", arrayOf(taskId.toString()))
        db.close()
    }
}