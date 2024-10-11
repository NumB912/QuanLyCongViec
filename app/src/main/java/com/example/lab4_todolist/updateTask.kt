package com.example.lab4_todolist

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab4_todolist.MainActivity.CustomAdapter
import com.example.lab4_todolist.databinding.ActivityUpdateTaskBinding

class updateTask : AppCompatActivity() {
   private lateinit var binding:ActivityUpdateTaskBinding
   private lateinit var db:TaskDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TaskDatabaseHelper(this)
       binding = ActivityUpdateTaskBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val id = intent.getIntExtra("ID",-1)
        val name = intent.getStringExtra("NAME")
        val description = intent.getStringExtra("DESCRIPTION")


        binding.editTask.setText(name)
        binding.editDecription.setText(description)


        binding.btnSubmit.setOnClickListener {
           val name = binding.editTask.text.toString()
            val description = binding.editDecription.text.toString()
            db.updateTask(Task(id,name,description))
            setResult(Activity.RESULT_OK)
            finish()

        }

    }

}