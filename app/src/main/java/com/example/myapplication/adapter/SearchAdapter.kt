package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dto.Todo
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(val context: Context,val list:ArrayList<Todo>) : RecyclerView.Adapter<SearchAdapter.TodoViewHolder>(),
    Filterable {


    override fun getFilter(): Filter {
        return exampleFilter
    }


    private val exampleFilter: Filter = object : Filter() {
        //background Thread 에서 자동으로
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterString = constraint.toString()

            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList = mutableListOf<Todo>()

            val result = FilterResults()

            Log.d(TAG, "검색값 : $constraint")

            //공백 제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {

                result.values = list
                result.count = list.size

                return result
            }

            //값이 있을 경우
            else {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }

                for (item in list) {
                    //filter 대상 setting
                    if (item.todo.contains(filterString)) {
                        Log.d(TAG, "검색필터: ${item.todo}")
                        filteredList.add(item)

                        Log.d(TAG, "검색 add list:  $filteredList")
                        result.values = filteredList
                        result.count = filteredList.size


                    } else {
                        Toast.makeText(context, " 검색 필터링 결과 무", Toast.LENGTH_SHORT).show()
                    }
                }
            }

//            result.values = filteredList
//            result.count=filteredList.size
//
//            Log.d(TAG,"검색 결과1: ${result.values}")
//            Log.d(TAG,"검색 결과2: $result")
//
//            return result
            Log.d(TAG, "검색 결과1: ${result.values}")
            Log.d(TAG, "검색 결과2: $result")

            return result
        }

        //UI Thread 에서 자동으로
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            list.clear()
            list.addAll(results?.values as ArrayList<Todo>)
            notifyDataSetChanged()
        }

    }


    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var timestamp = itemView.findViewById<TextView>(R.id.stamp)
        var content = itemView.findViewById<TextView>(R.id.content)

        fun onBind(data: Todo) {
            timestamp.text = data.stamp
            content.text = data.todo
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo_list, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }





}
