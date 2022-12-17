package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dto.Todo
import kotlin.collections.ArrayList

class SearchAdapter(val context: Context, val list: MutableList<Todo>) :
    RecyclerView.Adapter<SearchAdapter.TodoViewHolder>(),
    Filterable {

    var onItemLongClick: ((Todo) -> Unit)? = null
    var onItemClick: ((Todo) -> Unit)? = null

    var filteredTodo = ArrayList<Todo>()

    var exampleFilter = ExampleFilter()

    init {
        //원본 리스트를 다 참조해야 함함
        filteredTodo.addAll(list)
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

    inner class ExampleFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()
            Log.d(TAG, "charSequence : $charSequence")


            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList = ArrayList<Todo>()

            //공백제외 아무런 값이 없을 경우 -> 원본 배열

            TODO("서치뷰가 비었을 때 리스트가 다 지워지는 이슈")
            if (filterString.isEmpty()) {

                results.values = filteredTodo
                results.count =filteredTodo.size
                return results

                //공백제외 2글자 이하인 경우 -> 이름으로만 검색
            } else {
                for (item in list) {
                    if (item.todo.contains(filterString)) {
                        filteredList.add(item)

                        results.values = filteredList
                        results.count = filteredList.size

                    }
                }
                //그 외의 경우(공백제외 2글자 초과) -> 이름/전화번호로 검색
            }

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            (filterResults.values as ArrayList<Todo>?)?.let {
                filteredTodo.clear()
                filteredTodo.addAll(it)
                notifyDataSetChanged()
            }

        }
    }
    /*
    *     private val exampleFilter: Filter = object : Filter() {


        //background Thread 에서 자동으로
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString()

            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList: ArrayList<Todo> = ArrayList<Todo>()

            copylist = list as ArrayList<Todo>

            Log.d(TAG, "필터로그 : 1")

            Log.d(TAG, "검색값 : $constraint")

            val results = FilterResults()
            //공백 제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                Log.d(TAG, "필터로그 : 2")

//                result.values = filteredList
//                result.count = filteredList.size

//                filteredList.addAll(list)
                results.values = list
                results.count = list.size

                return results


            }

            //값이 있을 경우
            else {
                Log.d(TAG, "필터로그 : 3")
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }

                for (item in list) {
                    Log.d(TAG, "필터로그 : 4")
                    //filter 대상 setting
                    if (item.todo.contains(filterString)) {
                        Log.d(TAG, "필터로그 : 5")
                        Log.d(TAG, "검색필터: ${item.todo}")

                        filteredList.add(item)

                        //result.count = item

                        Log.d(TAG, "검색 add list:  $filteredList")


                        return results
                    }

                }
            }
            Log.d(TAG, "필터로그 : 6")
            results.values = filteredList
            results.count = filteredList.size

            return results

//
//            Log.d(TAG,"검색 결과1: ${result.values}")
//            Log.d(TAG,"검색 결과2: $result")
//
//            return result
//            Log.d(TAG, "검색 결과1: ${result.values}")
//            Log.d(TAG, "검색 결과2: $result")

//            val result = FilterResults()

        }


        //UI Thread 에서 자동으로
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//            list.clear()
//            // 리스트 클리어되고 add 되고 지우면 다시 리셋되야 하는데 안되고 그래로임,,,
//            (results?.values as ArrayList<Todo>?)?.let {
//
//                list.addAll(it)
//            }
            filteredTodo.clear()
            (results?.values as ArrayList<Todo>?)?.let { filteredTodo.addAll(it) }
            notifyDataSetChanged()
        }

    }*/


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
                onItemClick?.invoke(filteredTodo[adapterPosition])
                return@setOnClickListener
            }

            itemView.setOnLongClickListener {
                onItemLongClick?.invoke(filteredTodo[adapterPosition])
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
        holder.onBind(filteredTodo[position])
    }

    override fun getItemCount(): Int {
        return filteredTodo.size
    }

    fun update(newList: MutableList<Todo>) {
        this.filteredTodo = newList as ArrayList<Todo>
        notifyDataSetChanged()
    }


//    interface ItemClickListener {
//        fun onClick(view: View, position: Int, itemId: Long)
//    }
//
//    private lateinit var itemClickListner: ItemClickListener
//
//    fun setItemClickListener(itemClickListener: ItemClickListener) {
//        this.itemClickListner = itemClickListener
//    }

//    override fun getFilter(): Filter {
//
//        return exampleFilter
//    }
//
//    private val exampleFilter: Filter = object : Filter() {
//        //background Thread 에서 자동으로
//        override fun performFiltering(constraint: CharSequence?): FilterResults {
//
//            val filterString = constraint.toString()
//
//            //검색이 필요없을 경우를 위해 원본 배열을 복제
//            val filteredList = mutableListOf<Todo>()
//
//            val result = FilterResults()
//
//            Log.d(TAG, "검색값 : $constraint")
//
//            //공백 제외 아무런 값이 없을 경우 -> 원본 배열
//            if (filterString.trim { it <= ' ' }.isEmpty()) {
//
//                result.values = list
//                result.count = list.size
//
//                return result
//            }
//
//            //값이 있을 경우
//            else {
//                val filterPattern =
//                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
//
//                for (item in list) {
//                    //filter 대상 setting
//                    if (item.todo.contains(filterString)) {
//                        Log.d(TAG, "검색필터: ${item.todo}")
//                        filteredList.add(item)
//
//                        Log.d(TAG, "검색 add list:  $filteredList")
//                        result.values = filteredList
//                        result.count = filteredList.size
//
//
//                    } else {
//                        Toast.makeText(context, " 검색 필터링 결과 무", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
////            result.values = filteredList
////            result.count=filteredList.size
////
////            Log.d(TAG,"검색 결과1: ${result.values}")
////            Log.d(TAG,"검색 결과2: $result")
////
////            return result
//            Log.d(TAG, "검색 결과1: ${result.values}")
//            Log.d(TAG, "검색 결과2: $result")
//
//            return result
//        }
//
//        //UI Thread 에서 자동으로
//        @SuppressLint("NotifyDataSetChanged")
//        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//            list.clear()
//            list.addAll(results?.values as ArrayList<Todo>)
//            notifyDataSetChanged()
//        }
//
//    }

}