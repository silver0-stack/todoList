package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.SearchAdapter
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.databinding.ActivitySearchBinding
import com.example.myapplication.dto.Todo
import com.example.myapplication.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var todoViewModel: TodoViewModel
    lateinit var todoAdapter: TodoAdapter
    lateinit var searchAdapter: SearchAdapter
    lateinit var todo:MutableList<Todo>

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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)



        todoAdapter = TodoAdapter(this)



        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]




        todoViewModel.todTodoList.observe(this) {
            searchAdapter = SearchAdapter(this, it)
            searchAdapter.update(it)


            binding.searchList.layoutManager = LinearLayoutManager(this)
            binding.searchList.adapter = searchAdapter

            /*메모장 이동*/
            searchAdapter.onItemClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val intent = Intent(this@SearchActivity, MemoActivity::class.java)
                    intent.putExtra("item_update", it)
                    requestActivity.launch(intent)
                }

        }

        val searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextSubmit(query: String?): Boolean {

                    searchAdapter.filter.filter(query)
                    searchAdapter.notifyDataSetChanged()
                    binding.searchView.clearFocus()
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(newText: String?): Boolean {

                    searchAdapter.filter.filter(newText)
                    searchAdapter.notifyDataSetChanged()
                    return false
                }

            }


        //검색뷰에 바로 커서 적용
        binding.searchView.onActionViewExpanded()
        binding.searchView.setOnQueryTextListener(searchViewTextListener)


        val files=todoViewModel.todTodoList




        }


    }
}


