package com.example.task1

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.task1.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var todoEntityList = ArrayList<ToDoEntity>()
    var baseAdapterClass: BaseAdapterClass = BaseAdapterClass(todoEntityList)
    lateinit var todoDatabase:  ToDoDatabase
    var simpleDateFormat = SimpleDateFormat("dd/MM/YYYY, hh:mm:ss")
    var calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        todoDatabase = ToDoDatabase.getDatabaseInstance(this)
        getDatabaseValue()

        binding.listView.adapter = baseAdapterClass
        binding.listView.setOnItemLongClickListener { parent, view, position, id ->
            AlertDialog.Builder(this)
                .setTitle("Are you sure to delete")
                .setPositiveButton("yes"){_,_->
                    todoDatabase.todoDao().deleteToDoEntity(todoEntityList[position])
                    getDatabaseValue()
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No"){_,_->
                    Toast.makeText(this, "Not Deleted", Toast.LENGTH_SHORT).show()
                }
                .show()
            return@setOnItemLongClickListener true
        }


        binding.listView.setOnItemClickListener { parent, view, position, id ->
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.custom_alert_dialog_box)
            var etSelectDate = dialog.findViewById<EditText>(R.id.etSelectDate)
            var etSelectTime = dialog.findViewById<EditText>(R.id.etSelectTime)
            var etEnterTask = dialog.findViewById<EditText>(R.id.etEnterTask)
            var btnEdit = dialog.findViewById<Button>(R.id.btnEdit)
            var nextDate = Calendar.getInstance()
            nextDate.add(Calendar.MINUTE, +15)

            etSelectDate.setOnClickListener {

                DatePickerDialog(this,{ _, year, month, dateOfMonth,->

                    calendar.set(Calendar.YEAR,year)
                    calendar.set(Calendar.MONTH,month)
                    calendar.set(Calendar.DAY_OF_MONTH,dateOfMonth)
                    var formattedDate = simpleDateFormat.format(calendar.time)
                    etSelectDate.setText(formattedDate)

                },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            etSelectTime.setOnClickListener{

                TimePickerDialog(this, {_, hour, minute->
                    calendar.set(Calendar.HOUR, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    var formattedDate = simpleDateFormat.format(calendar.time)
                    etSelectTime.setText(formattedDate)
                },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), true  ).show()
            }



            btnEdit.setOnClickListener {
                if (etSelectDate.text.toString().trim().isNullOrEmpty()){
                    etSelectDate.error = "Enter Date"
                }else if (etEnterTask.text.toString().trim().isNullOrEmpty()){
                    etEnterTask.error = "Enter Task"
                }else if(calendar.after(nextDate)){
                    todoDatabase.todoDao().updateToDoEntity(
                        ToDoEntity(todoItem = etSelectDate.text.toString()+ etEnterTask.text.toString() ,
                            id = (todoEntityList[position].id )))
                    getDatabaseValue()
                    dialog.cancel()

                    Toast.makeText(this, "Edited ", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Can't Edit", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }
            }
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialog.show()

        }
        binding.fab.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.custom_dialog_box)
            var etSelectDate = dialog.findViewById<EditText>(R.id.etSelectDate)
            var etSelectTime = dialog.findViewById<EditText>(R.id.etSelectTime)
            var etEnterTask = dialog.findViewById<EditText>(R.id.etEnterTask)
            var btnSave = dialog.findViewById<Button>(R.id.btnSave)


            etSelectDate.setOnClickListener {

                DatePickerDialog(this,{ _, year, month, dateOfMonth,->
                    calendar.set(Calendar.YEAR,year)
                    calendar.set(Calendar.MONTH,month)
                    calendar.set(Calendar.DAY_OF_MONTH,dateOfMonth)
                    var formattedDate = simpleDateFormat.format(calendar.time)
                    etSelectDate.setText(formattedDate)

                },

                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            etSelectTime.setOnClickListener{
                TimePickerDialog(this, {_, hour, minute->
                    calendar.set(Calendar.HOUR, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    var formattedDate = simpleDateFormat.format(calendar.time)
                    etSelectTime.setText(formattedDate)
                },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), true  ).show()
            }


            btnSave.setOnClickListener {
                if (etSelectDate.text.toString().trim().isNullOrEmpty()){
                    etSelectDate.error = "Enter Date"
                }else if (etEnterTask.text.toString().trim().isNullOrEmpty()){
                    etEnterTask.error = "Enter Task"
                }else{
                    todoDatabase.todoDao().insertToDo(ToDoEntity(todoItem = etSelectDate.text.toString() + etEnterTask.text.toString()))
                    getDatabaseValue()
                    dialog.cancel()
                }
            }
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialog.show()
        }
    }
    fun getDatabaseValue(){
        todoEntityList.clear()
        todoEntityList.addAll(todoDatabase.todoDao().getToDoEntities())
        baseAdapterClass.notifyDataSetChanged()
    }

}