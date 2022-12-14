package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dto.Todo

class TodoAdapter(val context: Context) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private var list = mutableListOf<Todo>()

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

//        holder.itemView.setOnLongClickListener {
//            onItemClick!!.invoke(list[position])
//           //Toast.makeText(it.context, "$position 아이템 클릭!", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(newList: MutableList<Todo>) {
        this.list = newList
        notifyDataSetChanged()
    }


    interface ItemLongClickListener {
        fun onClick(view: View, position: Int, itemId: Long)
    }

    private lateinit var itemLongClickListener: ItemLongClickListener

    fun setLongClickListener(itemLongClickListener: ItemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener
    }
}