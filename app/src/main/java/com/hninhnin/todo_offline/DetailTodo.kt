package com.hninhnin.todo_offline

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hninhnin.todo_offline.databinding.ActivityDetailTodoBinding

class DetailTodo : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTodoBinding
    private var db: DatabaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailTodoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // back btn for action bar
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // get previous data by id
        val id = intent.getIntExtra("id", -1)
        val todo = db.getTodoById(id)
        binding.txtTitle.text = todo.title
        binding.txtDesc.text = todo.desc
        binding.txtDate.text = todo.date
        binding.txtTime.text = todo.time
        binding.txtStatus.text = todo.status
        binding.txtPlan.text = todo.taskPlan
        binding.txtPriority.text = todo.taskPriority
    }
}