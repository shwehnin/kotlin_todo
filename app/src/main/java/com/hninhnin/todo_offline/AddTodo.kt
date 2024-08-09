package com.hninhnin.todo_offline

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hninhnin.todo_offline.databinding.ActivityAddTodoBinding
import com.hninhnin.todo_offline.models.TodoModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTodo : AppCompatActivity() {
    private lateinit var binding: ActivityAddTodoBinding
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var rdButton : RadioButton
    private lateinit var high: CheckBox
    private lateinit var medium: CheckBox
    private lateinit var low: CheckBox
    private lateinit var resSpinner : String
    private var resCheckbox : String = ""
    private var resRadio : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)

        databaseHelper = DatabaseHelper(this)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnUpdate)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // back action btn
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // create date picker
        binding.imgDate.setOnClickListener {
            // create date picker dialog
            val datePickerDialog = DatePickerDialog(this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // create new calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                val formatDate = dateFormat.format(selectedDate.time)
                binding.txtDate.text = formatDate.toString()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        // create time picker
        binding.imgTime.setOnClickListener {
            // create time picker dialog
            val timePickerDialog = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                binding.txtTime.text = SimpleDateFormat("HH:mm").format(calendar.time)
            }

            TimePickerDialog(this, timePickerDialog, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        // set up the spinner
        val task = arrayOf("Pending", "Ongoing", "Completed", "Cancel")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, task)
        binding.todoSpinner.adapter = adapter

        // Set Spinner onItemSelectedListener
        binding.todoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                resSpinner = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        // create addTodo
        binding.btnSubmit.setOnClickListener {
            var title = binding.etTitle.text.trim().toString()
            var desc = binding.etDesc.text.trim().toString()
            var date = binding.txtDate.text.trim().toString()
            var time = binding.txtTime.text.trim().toString()
            high = binding.chkHigh
            medium = binding.chkMedium
            low = binding.chkLow

            // for checkbox task
            resCheckbox = "" // Reset resCheckbox to avoid duplicate values
            if(high.isChecked) {
                resCheckbox += "High\n"
            }

            if(medium.isChecked) {
                resCheckbox += "Medium\n"
            }

            if(low.isChecked) {
                resCheckbox += "Low\n"
            }

            // for radio group
            val checkedRadioButtonId = binding.rdGroup.checkedRadioButtonId
            if(checkedRadioButtonId != -1) {
                rdButton = findViewById(checkedRadioButtonId)
                resRadio = rdButton.text.toString()
            }else {
                resRadio = ""
            }

            var todo = TodoModel(0, title, desc, date, time, resRadio, resSpinner, resCheckbox)
            databaseHelper.insertData(todo)
            Toast.makeText(this, "Task data created successfully!", Toast.LENGTH_SHORT).show()
            finish()
            Log.d("Data", "${title} ${desc} ${date} ${time}")
        }
    }
}