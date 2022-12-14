package com.example.myapplication

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.databinding.ActivityTodayBinding
import com.example.myapplication.dto.Todo
import com.example.myapplication.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TodayActivity : AppCompatActivity() {
    lateinit var binding: ActivityTodayBinding
    lateinit var todoAdapter: TodoAdapter
    lateinit var todoViewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        /*todoViewModel.todoList 는 LiveData 이기 때문에 observe 를 통해 변화된 값을 감지할 수 있음.
          이를 이용해 목록의 값이 변경(추가, 수정, 삭제)되면 이를 TodoAdapter 에 반영하여 데이터가 업데이트 되도록 함.
        */
        todoViewModel.todTodoList.observe(this) {
            todoAdapter.update(it)
        }
        todoAdapter = TodoAdapter(this)

        binding.todolist.layoutManager=LinearLayoutManager(this)
        binding.todolist.adapter=todoAdapter
        //binding.todolist.setHasFixedSize(true)

        val builder = AlertDialog.Builder(this)

        todoAdapter.setLongClickListener(object: TodoAdapter.ItemLongClickListener {

            override fun onClick(view: View, position: Int, itemId: Long) {

                Log.d("adapter","$position 클릭!")
                val todo = todoViewModel.getOne(itemId)

                builder
                    .setTitle("이 메모를 삭제하시겠습니까?")
                    .setMessage(todo.toString())
                    .setPositiveButton("삭제") { dialogInterface: DialogInterface, i: Int ->

                        CoroutineScope(Dispatchers.IO).launch {
                            todoViewModel.delete(todo)
                        }

                    }
                    .setNegativeButton("유지") { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }

                // 다이얼로그를 띄워주기
                builder.create().show()


            }
        })


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("a h:mm")
        val timestamp = current.format(formatter)

        val todo = Todo(0, timestamp, "메모를 작성하세요...")

        binding.todAdd.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                /*viewModel -> todoRepository -> todoDao 순으로 타고 들어가 데이터베이스에 저장*/
                todoViewModel.insert(todo)
            }
            Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
        }

    }
}