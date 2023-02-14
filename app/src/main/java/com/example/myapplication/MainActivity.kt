package com.example.myapplication

import MyTouchHelperCallback
import android.app.SearchManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var todoAdapter: TodoAdapter
    lateinit var todoViewModel: TodoViewModel

    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("a h:mm")





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
                        Toast.makeText(this, "추가되었습니다", Toast.LENGTH_SHORT).show()
                    }
                    /*수정한 후*/
                    1 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            runOnUiThread {
                                todoViewModel.update(todo)
                            }
                        }
                        Toast.makeText(this, "수정되었습니다", Toast.LENGTH_SHORT).show()
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



        todoAdapter = TodoAdapter(this)

        binding.todolist.layoutManager = LinearLayoutManager(this)
        binding.todolist.adapter = todoAdapter

        val callback=MyTouchHelperCallback(todoAdapter)
        val touchHelper=ItemTouchHelper(callback)
        // ItemTouchHelper를 RecyclerView에 연결
        touchHelper.attachToRecyclerView(binding.todolist)

        todoAdapter.itemDragListener(object : MyTouchHelperCallback.ItemStartDragListener {
            override fun onDropActivity(initList: ArrayList<Todo>, changeList: ArrayList<Todo>) {
                // TODO : 드랍됐을 때 처리
                println(initList) // 최초 리스트
                println(changeList) // Drag and Drop 이후 리스트
                println("------ \n")
            }

      })


        /*메모장 이동*/
        todoAdapter.onItemClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val intent = Intent(this@MainActivity, MemoActivity::class.java)
                intent.putExtra("item_update", it)
                requestActivity.launch(intent)
            }

        }
        /*아이템 삭제*/
        todoAdapter.onItemLongClick = {
            CoroutineScope(Dispatchers.IO).launch {
                runOnUiThread {
                    val builder = AlertDialog.Builder(this@MainActivity)

                    builder
                        .setTitle("이 메모를 삭제하시겠습니까?")
                        .setMessage(it.todo)
                        .setPositiveButton("삭제") { dialogInterface: DialogInterface, i: Int ->
                            todoViewModel.delete(it)

                        }
                        .setNegativeButton("유지") { dialogInterface: DialogInterface, i: Int ->
                            dialogInterface.dismiss()
                        }

                    // 다이얼로그를 띄워주기
                    builder.create().show()
                }
            }


        }


        handleIntent(intent)

        //툴바 설정
        setSupportActionBar(binding.toolbar)


        binding.nav.itemIconTintList = null

        //Drawer 토글버튼 생성
        val barDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.layoutDrawer,
            binding.toolbar,
            R.string.app_name,
            R.string.app_name
        )
        //삼선아이콘 모양으로 보이기, 동기맞춤
        barDrawerToggle.syncState()
        //삼선아이콘 화살표 아이콘 자동 변환
        binding.layoutDrawer.addDrawerListener(barDrawerToggle)


        //네비게시션뷰의 아이템 클릭 시
        binding.nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.login -> {
                    Toast.makeText(this, "로그인 클릭", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    binding.layoutDrawer.closeDrawer(binding.nav)
                    false
                }

            }

        }

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

                //Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()

                true
            }

            R.id.search -> {
                Toast.makeText(this, "search menu clicked!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleIntent(intent: Intent?) {

        if (Intent.ACTION_SEARCH == intent!!.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        super.onCreateOptionsMenu(menu)


        return false
    }


}