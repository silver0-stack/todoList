package com.example.myapplication

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isNotEmpty
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale.filter

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var searchAdapter: SearchAdapter
    lateinit var todoViewModel: TodoViewModel

    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("a h:mm")
    val timestamp = current.format(formatter)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=Intent()
        val list=intent.getSerializableExtra("list")
        Log.d("리스트 전달: ", list as String)

        val searchViewTextListener:SearchView.OnQueryTextListener=object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                if(newText!!.isNotEmpty()){
//                    searchAdapter.filter.filter(newText)
//                }

                return false
            }

        }

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        /*todoViewModel.todoList 는 LiveData 이기 때문에 observe 를 통해 변화된 값을 감지할 수 있음.
          이를 이용해 목록의 값이 변경(추가, 수정, 삭제)되면 이를 TodoAdapter 에 반영하여 데이터가 업데이트 되도록 함.
        */

        searchAdapter = SearchAdapter(this, list as ArrayList<Todo>)

        binding.searchList.layoutManager = LinearLayoutManager(this)
        binding.searchList.adapter = searchAdapter

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        //검색뷰에 바로 커서 적용
        binding.searchView.onActionViewExpanded()

        binding.searchView.setOnQueryTextListener(searchViewTextListener)



    }


}


