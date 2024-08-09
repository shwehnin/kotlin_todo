package com.hninhnin.todo_offline

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hninhnin.todo_offline.databinding.ActivityMainBinding
import com.hninhnin.todo_offline.data.TodoAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        db = DatabaseHelper(this)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnUpdate)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // go to add to do page
        binding.addTodo.setOnClickListener {
            startActivity(Intent(this, AddTodo::class.java))
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvTodo.layoutManager = layoutManager

        adapter = TodoAdapter(db.getAllTodo(), this)
        binding.rvTodo.adapter = adapter


    }

    override fun onResume() {
        adapter.refreshData(db.getAllTodo())
        super.onResume()
    }
}