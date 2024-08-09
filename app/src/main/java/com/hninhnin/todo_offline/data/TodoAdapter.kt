package com.hninhnin.todo_offline.data

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hninhnin.todo_offline.DatabaseHelper
import com.hninhnin.todo_offline.DetailTodo
import com.hninhnin.todo_offline.R
import com.hninhnin.todo_offline.UpdateTodo
import com.hninhnin.todo_offline.models.TodoModel

class TodoAdapter(var todoList: List<TodoModel>,var context: Context) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){
    // DB
    private val db: DatabaseHelper = DatabaseHelper(context)

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.cardTitle)
        val date = itemView.findViewById<TextView>(R.id.cardDate)
        var imgEdit = itemView.findViewById<ImageView>(R.id.imgEdit)
        var imgDelete = itemView.findViewById<ImageView>(R.id.imgDelete)
        var todoCard = itemView.findViewById<ConstraintLayout>(R.id.todoCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_card, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        var todo = todoList[position]
        holder.title.text = todo.title
        holder.date.text = todo.date

        // todoEdit
        holder.imgEdit.setOnClickListener {
            val intent = Intent(context, UpdateTodo::class.java)
            intent.putExtra("id", todo.id)
            context.startActivity(intent)
        }

        //todoDelete
        holder.imgDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure want to delete for this item")
            builder.setCancelable(false) // touch outside area, dismiss dialog
            builder.setPositiveButton("OK") {dialog, which ->
                //delete todoById
                db.deleteTodo(todo.id)
                // refresh todolist
                refreshData(db.getAllTodo())
                Toast.makeText(context, "Todo item deleted successfully!", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("Cancel") {dialog, which ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        // get details for todoData
        holder.todoCard.setOnClickListener {
            val intent = Intent(context, DetailTodo::class.java)
            intent.putExtra("id", todo.id)
            context.startActivity(intent)
        }

    }

    // refresh data list
    fun refreshData(newTodo: List<TodoModel>) {
        todoList = newTodo
        notifyDataSetChanged()
    }
}