package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
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
    lateinit var todo: MutableList<Todo>

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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        todoAdapter = TodoAdapter(this, todoViewModel)








        todoViewModel.todTodoList.observe(this) { it ->
            searchAdapter = SearchAdapter(this, it)
            searchAdapter.update(it)


            binding.searchList.layoutManager = LinearLayoutManager(this)
            binding.searchList.adapter = searchAdapter

            /*메모장 이동*/
            searchAdapter.onItemClick = {
                showDialog(it)

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


        }


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
                val intent = Intent(this@SearchActivity, MemoActivity::class.java)
                intent.putExtra("item_update", Todo)
                requestActivity.launch(intent)
            }
            dialog.dismiss()
        }
        deleteBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                todoViewModel.delete(Todo)
            }
            Toast.makeText(this@SearchActivity, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()

    }

}


