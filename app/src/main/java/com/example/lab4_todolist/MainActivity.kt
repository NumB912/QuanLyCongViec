package com.example.lab4_todolist
import android.app.Activity
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var taskDatabaseHelper: TaskDatabaseHelper
    private lateinit var listView: ListView
    private lateinit var btnAddTask: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskDatabaseHelper = TaskDatabaseHelper(this)


        listView = findViewById(R.id.listView)
        btnAddTask = findViewById(R.id.btnAddTask)
        val editTextTask = findViewById<EditText>(R.id.editTask)
        val editDecription = findViewById<EditText>(R.id.editDecription)
        btnAddTask.setOnClickListener {
            if(editTextTask.text.toString()!=""&&editDecription.text.toString()!=""){
                taskDatabaseHelper.addTask(Task(1,editTextTask.text.toString(),editDecription.text.toString()))
            }
             loadTasks()
        }

        loadTasks()
    }

    fun loadTasks() {
        val tasks = taskDatabaseHelper.getAllTasks()
        val adapter = CustomAdapter(this,tasks,taskDatabaseHelper)
        listView.adapter = adapter
    }

    class CustomAdapter(
        context: MainActivity,
        private var tasks: List<Task>,
        private val taskDatabaseHelper: TaskDatabaseHelper
    ) : ArrayAdapter<Task>(context, 0, tasks) {


        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val item = getItem(position)
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.task_list, parent, false)

            val titleTextView: TextView = view.findViewById(R.id.txtTask)
            val itemDescription: TextView = view.findViewById(R.id.txtDescription)
            val buttonItemDelete: Button = view.findViewById(R.id.btnDelete)
            val buttonItemUpdate: Button = view.findViewById(R.id.btnUpdate)

            titleTextView.text = item?.name
            itemDescription.text = item?.description

            buttonItemDelete.setOnClickListener {
                item?.let {
                    taskDatabaseHelper.deleteTask(item.ID) // Gọi deleteTask với ID
                    Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                    (context as MainActivity).loadTasks() // Cập nhật lại danh sách sau khi xóa
                }
            }
            buttonItemUpdate.setOnClickListener {
                val intent = Intent(context,updateTask::class.java)
                intent.putExtra("ID",item?.ID)
                intent.putExtra("NAME",item?.name)
                intent.putExtra("DESCRIPTION",item?.description)
                (context as MainActivity).startActivityForResult(intent, REQUEST_CODE)
            }


            return view
        }




    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadTasks() // Tải lại danh sách khi quay lại
        }
    }

    companion object {
        private const val REQUEST_CODE = 1 // Mã yêu cầu để phân biệt Activity
    }

}
