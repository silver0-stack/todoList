package com.example.myapplication

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
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

    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("a h:mm")
    val timestamp = current.format(formatter)


    private val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val todo = it.data?.getSerializableExtra("todo") as Todo

                when (it.data?.getIntExtra("flag", -1)) {
                    0 -> {

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
        binding = ActivityTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val searchIntent = Intent(this, SearchActivity::class.java)


        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        /*todoViewModel.todoList 는 LiveData 이기 때문에 observe 를 통해 변화된 값을 감지할 수 있음.
          이를 이용해 목록의 값이 변경(추가, 수정, 삭제)되면 이를 TodoAdapter 에 반영하여 데이터가 업데이트 되도록 함.
        */
        todoViewModel.todTodoList.observe(this) {
            todoAdapter.update(it)

            //서치액티비티로 리스트 전달
            searchIntent.putExtra("list", it as ArrayList<Todo>)
        }


        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add -> {
                    val todo = Todo(0, timestamp, "")
                    CoroutineScope(Dispatchers.IO).launch {
                        /*viewModel -> todoRepository -> todoDao 순으로 타고 들어가 데이터베이스에 저장*/
                        todoViewModel.insert(todo)
                    }
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()

                    return@setOnMenuItemClickListener true

                }
                R.id.search -> {
                    Toast.makeText(this, "search menu clicked!", Toast.LENGTH_SHORT).show()
                    startActivity(searchIntent)

                    return@setOnMenuItemClickListener true
                }

                else -> {
                    return@setOnMenuItemClickListener false
                }

            }


        }


        todoAdapter = TodoAdapter(this)

        binding.todolist.layoutManager = LinearLayoutManager(this)
        binding.todolist.adapter = todoAdapter
        //binding.todolist.setHasFixedSize(true)


//        todoAdapter.setItemClickListener(object : TodoAdapter.ItemClickListener {
//            override fun onClick(view: View, position: Int, itemId: Long) {
//                Toast.makeText(this@TodayActivity, "$itemId", Toast.LENGTH_SHORT).show()
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    runOnUiThread {
//                        val todo = todoViewModel.getOne(itemId)
//
//                        val intent = Intent(this@TodayActivity, MemoActivity::class.java).apply {
//                            putExtra("item", todo)
//                        }
//                        requestActivity.launch(intent)
//                    }
//
//                }
//            }
//        })

        /*메모장 이동*/
        todoAdapter.onItemClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val intent = Intent(this@TodayActivity, MemoActivity::class.java)
                intent.putExtra("item", it)
                requestActivity.launch(intent)
            }

        }
        /*아이템 삭제*/
        todoAdapter.onItemLongClick = {
            CoroutineScope(Dispatchers.IO).launch {
                runOnUiThread {
                    val builder = AlertDialog.Builder(this@TodayActivity)

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

        val searchItem: MenuItem? = menu?.findItem(R.id.search)

//
//
//        //검색을 누른 후 옵션 아이콘을 누른 후 검색이 끝나면 검색 아이콘이 사라져서 넣음
//        menu.findItem(R.id.search)
//            ?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
//                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
//                    //   menu.findItem(R.id.add)!!.isVisible=false
//
//                    //검색뷰 클릭 시 가로폭 최대치로 설정
//                    searchView.maxWidth = Integer.MAX_VALUE
//                    //검색 버튼 클릭했을 때 서치뷰에 힌트 추가
//                    searchView.queryHint = "검색..."
//
//                    return true
//                }
//
//                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
//                    invalidateOptionsMenu()
//                    return true
//                }
//
//            })
//
////        //검색뷰 클릭 시 가로폭 최대치로 설정
////        searchView.maxWidth = Integer.MAX_VALUE
////        //검색 버튼 클릭했을 때 서치뷰에 힌트 추가
////        searchView.queryHint = "검색..."
//
//
//        /*검색 가능한 구성을 SearchView 와 연결*/
//        //서치뷰와 searchable 설정 결합시키기
//
//        searchView.apply {
//            setSearchableInfo(searchManager.getSearchableInfo(componentName))
//            setIconifiedByDefault(true)
//
//            this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    todoAdapter.getFilter().filter(query)
//                    return true
//                }
//
//                //텍스트 입력/수정 시에 호출
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    return true
//                }
//
//            })
//        }
        return false
    }


}