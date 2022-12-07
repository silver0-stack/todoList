package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dto.tod_Todo
import com.example.myapplication.repository.tod_TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/* viewModel 은 액티비티나 프래그먼트의 lifecycle 과 별개로 돌아가기 때문에 데이터의 유지 및 공유가 가능하다
* 이에 따라 viewModel 에서 CRUD 를 사용해 액티비티의 이동이 있어도 동일하게 값을 불러올 수 있도록 하였음*/

class tod_TodoViewModel: ViewModel() {

    val todTodoList:LiveData<MutableList<tod_Todo>>
    private val todoRepository: tod_TodoRepository =tod_TodoRepository.get()

    init {
        todTodoList=todoRepository.list()
    }

    fun getOne(id:Long)=todoRepository.getTodo(id)

    fun insert(dto:tod_Todo)=viewModelScope.launch(Dispatchers.IO){
        todoRepository.insert(dto)
    }

    fun update(dto:tod_Todo)=viewModelScope.launch(Dispatchers.IO){
        todoRepository.update(dto)
    }

    fun delete(dto:tod_Todo)=viewModelScope.launch(Dispatchers.IO){
        todoRepository.delete(dto)
    }


}