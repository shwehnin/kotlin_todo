package com.hninhnin.todo_offline

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hninhnin.todo_offline.databinding.ActivityUpdateTodoBinding
import com.hninhnin.todo_offline.models.TodoModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateTodo : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateTodoBinding
    private var db: DatabaseHelper = DatabaseHelper(this)
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var resSpinner : String
    private var resCheckbox : String = ""
    private lateinit var high: CheckBox
    private lateinit var medium: CheckBox
    private lateinit var low: CheckBox
    private var resRadio : String = ""
    private lateinit var rdButton : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateTodoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnUpdate)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // back btn for action bar
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // create date picker
        binding.imgDate.setOnClickListener {
            //create date picker dialog
            val datePickerDialog = DatePickerDialog(this, {DatePicker, year:Int, monthOfYear: Int, dayOfMonth: Int ->
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
            var timePickerDialog = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                binding.txtTime.text = SimpleDateFormat("HH:mm").format(calendar.time)
            }
            TimePickerDialog(this, timePickerDialog, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        // get previous data by id
        var id = intent.getIntExtra("id", -1)
        var todo = db.getTodoById(id)
        binding.etTitle.setText(todo.title)
        binding.etDesc.setText(todo.desc)
        binding.txtDate.text = todo.date
        binding.txtTime.text = todo.time


        // spinner setup
        val task = arrayOf("Pending", "Ongoing", "Completed", "Cancel")
        // set up the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, task)
        binding.todoSpinner.adapter = adapter

        // spinner value
        val position = task.indexOf(todo.taskPlan)
        binding.todoSpinner.setSelection(position)

//        // Set Spinner onItemSelectedListener
        binding.todoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               resSpinner  = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                resSpinner = ""
            }

        }

        // checkbox values
        val checkboxValues = todo.taskPriority.split("\n")
        high = binding.chkHigh
        medium = binding.chkMedium
        low = binding.chkLow
        high.isChecked = checkboxValues.contains("High")
        medium.isChecked = checkboxValues.contains("Medium")
        low.isChecked = checkboxValues.contains("Low")


        // radio button setup
        val radioButtons = listOf(binding.rdOn, binding.rdOff)
        for(radioButton in radioButtons) {
            if(radioButton.text.toString() == todo.status) {
                radioButton.isChecked = true
                break
            }
        }

        // update todoData
        binding.btnEdit.setOnClickListener {
            // for checkbox value
            // for checkbox task
            resCheckbox = "" // Reset resCheckbox to avoid duplicate values
            if (high.isChecked) resCheckbox += "High\n"
            if(medium.isChecked) resCheckbox += "Medium\n"
            if(low.isChecked) resCheckbox += "Low\n"

            // for radio group
            val checkedRadioButtonId = binding.rdGroup.checkedRadioButtonId
            if(checkedRadioButtonId != -1) {
                rdButton = findViewById(checkedRadioButtonId)
               resRadio =  rdButton.text.toString()
            }else {
                resRadio = ""
            }

            resSpinner = binding.todoSpinner.selectedItem.toString()

            var title = binding.etTitle.text.toString()
            var desc = binding.etDesc.text.toString()
            var date = binding.txtDate.text.toString()
            var time = binding.txtTime.text.toString()
            var todo = TodoModel(id, title, desc, date, time, resRadio, resSpinner, resCheckbox)
            Log.d("Updated todo", todo.toString())
            db.updateTodo(todo)
            finish()
            Toast.makeText(this, "Todo data updated successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}