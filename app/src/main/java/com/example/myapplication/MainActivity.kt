package com.example.myapplication


import android.app.Dialog
import android.app.SearchManager
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.dto.Todo
import com.example.myapplication.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.AccessController.getContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var todoAdapter: TodoAdapter
    lateinit var todoViewModel: TodoViewModel

    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("a h:mm")


    private var list = mutableListOf<Todo>()

    private val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val todo = it.data?.getSerializableExtra("todo") as Todo

                when (it.data?.getIntExtra("flag", -1)) {
                    /*추가한 후*/
                    0 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            runOnUiThread {
                                todoViewModel.insert(todo)
                            }
                        }
                    }
                    /*수정한 후*/
                    1 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            runOnUiThread {
                                todoViewModel.update(todo)
                            }
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val searchIntent = Intent(this, SearchActivity::class.java)


        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        /*todoViewModel.todoList 는 LiveData 이기 때문에 observe 를 통해 변화된 값을 감지할 수 있음.
          이를 이용해 목록의 값이 변경(추가, 수정, 삭제)되면 이를 TodoAdapter 에 반영하여 데이터가 업데이트 되도록 함.
        */
        todoViewModel.todTodoList.observe(this) {
            todoAdapter.update(it)

        }


        todoAdapter = TodoAdapter(this, todoViewModel)

        binding.todolist.layoutManager = LinearLayoutManager(this)
        binding.todolist.adapter = todoAdapter


        /*메모장 이동*/
        todoAdapter.onItemClick = {
            showDialog(it)
        }
        /*아이템 삭제*/
        todoAdapter.onItemLongClick = {
            showDialog(it)
        }


        //툴바 설정
        setSupportActionBar(binding.toolbar)


    }


    private fun showDialog(Todo: Todo) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        /**
         *  백그라운드 컬러 투명 (이걸 해줘야 background 가 설정해준 모양으로 변함)
         */
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog)
        val body = dialog.findViewById(R.id.todo) as TextView
        body.text = Todo.todo
        val editBtn = dialog.findViewById(R.id.btn_edit) as Button
        val deleteBtn = dialog.findViewById(R.id.btn_delete) as Button

        editBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val intent = Intent(this@MainActivity, MemoActivity::class.java)
                intent.putExtra("item_update", Todo)
                requestActivity.launch(intent)
            }
            dialog.dismiss()
        }
        deleteBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                todoViewModel.delete(Todo)
            }
            Toast.makeText(this@MainActivity, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.add -> {
                val timestamp = current.format(formatter)
                val todo = Todo(0, timestamp, " ")
                CoroutineScope(Dispatchers.IO).launch {
                    /*viewModel -> todoRepository -> todoDao 순으로 타고 들어가 데이터베이스에 저장*/

                    //todoViewModel.insert(todo)
                    val intent = Intent(this@MainActivity, InsertActivity::class.java)
                    intent.putExtra("item_insert", todo)
                    requestActivity.launch(intent)
                }


                true
            }

            R.id.mode -> {
                when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }

                true
            }

            R.id.search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        super.onCreateOptionsMenu(menu)
        return false
    }


}