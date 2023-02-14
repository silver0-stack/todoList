package com.example.myapplication.adapter

import MyTouchHelperCallback
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.MemoActivity
import com.example.myapplication.dto.Todo
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.ArrayList

class TodoAdapter(val context: Context) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(),MyTouchHelperCallback.OnItemMoveListener{
    private var list = mutableListOf<Todo>()
    var copylist = ArrayList<Todo>()
    var onItemLongClick: ((Todo) -> Unit)? = null
    var onItemClick: ((Todo) -> Unit)? = null
    private var onItemDragListener: MyTouchHelperCallback.ItemStartDragListener? = null
   //private lateinit var dragListener: OnStartDragListener

    var initList: ArrayList<Todo> = ArrayList()
    private var mSampleList: ArrayList<Todo> = ArrayList()

    /**
     * Activity 에서 호출할 메서드
     */
    fun itemDragListener(startDrag: MyTouchHelperCallback.ItemStartDragListener) {
        this.onItemDragListener = startDrag
    }
//
//    /*drag 가 시작될 때 drag 를 하도록 해야 하므로 리스너 생성*/
//    interface OnStartDragListener {
//        fun onStartDrag(viewHolder:TodoViewHolder)
//    }

    /**
     * Item이 바뀌면 리스트에 적용
     */
    override fun onItemMove(fromPosition: Int, toPosition: Int) {

        //collection.swap 을 통해 변경된 아이템을 교체하고
        //notifyItemMoved 를 이용해서 아이템 변경을 알림림
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    /**
     * Drag and Drop하여 ViewHolder가 변경될 때 호출
     */
    override fun onDropAdapter() {
        onItemDragListener?.onDropActivity(initList,mSampleList)
    }

    override fun onItemSwiped(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

//    fun startDrag(listener: OnStartDragListener) {
////        this.dragListener = listener
////    }


    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var timestamp = itemView.findViewById<TextView>(R.id.stamp)
        var content = itemView.findViewById<TextView>(R.id.content)

        fun onBind(data: Todo) {


            timestamp.text = data.stamp
            content.text = data.todo

//            itemView.setOnClickListener {
//                itemClickListner.onClick(it, layoutPosition, list[layoutPosition].id)
//            }
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
//    fun startDrag(listener: OnStartDragListener) {
//        this.dragListener = listener
//    }
    fun update(newList: MutableList<Todo>) {
        this.list = newList
        notifyDataSetChanged()
    }


}