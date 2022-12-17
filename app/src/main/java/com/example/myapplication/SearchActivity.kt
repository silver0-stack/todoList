package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.SearchAdapter
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.databinding.ActivitySearchBinding
import com.example.myapplication.dto.Todo
import com.example.myapplication.viewmodel.TodoViewModel


class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var todoViewModel: TodoViewModel
    lateinit var todoAdapter: TodoAdapter
    lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        todoAdapter = TodoAdapter(this)


        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]




        todoViewModel.todTodoList.observe(this) {
            searchAdapter=SearchAdapter(this, it)
            searchAdapter.update(it)


            binding.searchList.layoutManager = LinearLayoutManager(this)
            binding.searchList.adapter = searchAdapter

        }

        val searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                            searchAdapter.filter.filter(newText)


                    return false
                }

            }




        //검색뷰에 바로 커서 적용
        binding.searchView.onActionViewExpanded()
        binding.searchView.setOnQueryTextListener(searchViewTextListener)


    }


}


