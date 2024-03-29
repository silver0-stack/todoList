package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dto.Todo
import com.example.myapplication.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/* viewModel 은 액티비티나 프래그먼트의 lifecycle 과 별개로 돌아가기 때문에 데이터의 유지 및 공유가 가능하다
* 이에 따라 viewModel 에서 CRUD 를 사용해 액티비티의 이동이 있어도 동일하게 값을 불러올 수 있도록 하였음*/

class TodoViewModel: ViewModel() {

    var todTodoList:LiveData<MutableList<Todo>>
    private val todoRepository: TodoRepository =TodoRepository.get()

    init {
        todTodoList=todoRepository.list()
    }

    fun getOne(id:Long)=todoRepository.getTodo(id)

    fun insert(dto:Todo)=viewModelScope.launch(Dispatchers.IO){
        todoRepository.insert(dto)
    }

    fun update(dto:Todo)=viewModelScope.launch(Dispatchers.IO){
        todoRepository.update(dto)
    }

    fun delete(dto:Todo)=viewModelScope.launch(Dispatchers.IO){
        todoRepository.delete(dto)
    }


}