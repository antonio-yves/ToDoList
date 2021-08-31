package br.com.antonio.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import br.com.antonio.todolist.databinding.ActivityAddTaskBinding
import br.com.antonio.todolist.datasource.TaskDataSource
import br.com.antonio.todolist.extensions.format
import br.com.antonio.todolist.extensions.text
import br.com.antonio.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.titleTask.text = it.title
                binding.dateTask.text = it.date
                binding.hourTask.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.dateTask.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.dateTask.text = (Date(it + offset).format())
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.hourTask.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.hourTask.text = "${hour}:${minute}"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnCreateTask.setOnClickListener {
            val task = Task(
                title = binding.titleTask.text,
                date = binding.dateTask.text,
                hour = binding.hourTask.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}