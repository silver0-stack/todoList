package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.databinding.ActivityTodayBinding
import com.example.myapplication.dto.Todo
import com.example.myapplication.viewmodel.TodoViewModel
import com.google.android.material.timepicker.TimeFormat
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

        /*todoViewModel.todoList는 LiveData이기 때문에 observe를 통해 변화된 값을 감지할 수 있습니다.
          이를 이용해 목록의 값이 변경(추가, 수정, 삭제)되면 이를 TodoAdapter에 반영하여 데이터가 업데이트 되도록 하겠습니다.
        */
        todoViewModel.todTodoList.observe(this) {
            todoAdapter.update(it)
        }
        todoAdapter = TodoAdapter(this)

        binding.todList.layoutManager=LinearLayoutManager(this)
        binding.todList.adapter=todoAdapter

        todoAdapter.setItemCheckBoxClickListener(object: TodoAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getOne(itemId)
                    todoViewModel.update(todo)
                }
            }
        })

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
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