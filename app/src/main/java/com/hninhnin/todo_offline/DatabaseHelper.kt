package com.hninhnin.todo_offline

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hninhnin.todo_offline.models.TodoModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "task.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "todo"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESC = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_TASK_PLAN = "task_plan"
        private const val COLUMN_TASK_PRIORITY = "task_priority"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_DESC TEXT, $COLUMN_DATE TEXT, $COLUMN_TIME TEXT, $COLUMN_STATUS TEXT, $COLUMN_TASK_PLAN TEXT, $COLUMN_TASK_PRIORITY TEXT)"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    // create todoData
    fun insertData(todo: TodoModel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, todo.title)
            put(COLUMN_DESC, todo.desc)
            put(COLUMN_DATE, todo.date)
            put(COLUMN_TIME, todo.time)
            put(COLUMN_STATUS, todo.status)
            put(COLUMN_TASK_PLAN, todo.taskPlan)
            put(COLUMN_TASK_PRIORITY, todo.taskPriority)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // get all todolist
    fun getAllTodo() : List<TodoModel> {
        val noteList = mutableListOf<TodoModel>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            val status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
            val taskPlan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_PLAN))
            val taskPriority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_PRIORITY))
            val todo = TodoModel(id, title, description, date, time, status, taskPlan, taskPriority)
            noteList.add(todo)
        }
        cursor.close()
        db.close()
        return noteList
    }

    // delete todoData
    fun deleteTodo(todoID: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(todoID.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    // get todoBy id
    fun getTodoById(todoId: Int) : TodoModel {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=$todoId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        var id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        var title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        var desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC))
        var date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
        var time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
        var taskPlan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
        var taskPriority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_PLAN))
        var status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_PRIORITY))
        var todo = TodoModel(id, title, desc, date, time, taskPlan, taskPriority, status)
        db.close()
        return todo
    }

    // update todoData
    fun updateTodo(todo: TodoModel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, todo.title)
            put(COLUMN_DESC, todo.desc)
            put(COLUMN_DATE, todo.date)
            put(COLUMN_TIME, todo.time)
            put(COLUMN_STATUS, todo.status)
            put(COLUMN_TASK_PLAN, todo.taskPlan)
            put(COLUMN_TASK_PRIORITY, todo.taskPriority)
        }
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(todo.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
}