package com.example.myapplication.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.MemoActivity
import com.example.myapplication.dto.Todo
import com.example.myapplication.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class TodoAdapter(val context: Context, val viewModel: TodoViewModel) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){
    private var list = mutableListOf<Todo>()
    var onItemLongClick: ((Todo) -> Unit)? = null
    var onItemClick: ((Todo) -> Unit)? = null


    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp = itemView.findViewById<TextView>(R.id.stamp)
        var content = itemView.findViewById<TextView>(R.id.content)

        fun onBind(data: Todo) {
            timestamp.text = data.stamp
            content.text = data.todo
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
                return@setOnClickListener
            }

            itemView.setOnLongClickListener {
                onItemLongClick?.invoke(list[adapterPosition])
                return@setOnLongClickListener true
            }
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

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: MutableList<Todo>) {
        this.list = newList
        notifyDataSetChanged()
    }


}