package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.databinding.ActivityWritingBinding
import com.example.myapplication.dto.Todo
import com.example.myapplication.viewmodel.TodoViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MemoActivity : AppCompatActivity() {
    lateinit var binding: ActivityWritingBinding
    private var todo: Todo? = null
    private var backBtnTime:Long=0

    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("a h:mm")
    val timestamp = current.format(formatter)


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todo = intent.getSerializableExtra("item_update") as Todo?
        binding.writing.setText(todo!!.todo)



        binding.back.setOnClickListener {
            val curTime=System.currentTimeMillis()
            val gapTime=curTime-backBtnTime

            //gapTime 이 0~2초 사이에 한 번 더 누르면
            //뒤로 가기 실행되고 아니면 토스트
            if (gapTime in 0..2000){
                val todo = Todo(todo!!.id, timestamp, binding.writing.text.toString())

                val intent = Intent().apply {
                    putExtra("todo", todo)
                    // MainActivity 로 다시 날려줌
                    putExtra("flag", 1)
                }
                setResult(RESULT_OK, intent)
                finish()
            }else{
                backBtnTime=curTime
                Toast.makeText(this,"한번 더 누르면 목록으로 이동합니다",Toast.LENGTH_SHORT).show()

                binding.writing.isCursorVisible=false

            }

        }


    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val curTime=System.currentTimeMillis()
        val gapTime=curTime-backBtnTime

        //gapTime 이 0~2초 사이에 한 번 더 누르면
        //뒤로 가기 실행되고 아니면 토스트
        if (gapTime in 0..2000){
            val todo = Todo(todo!!.id, timestamp, binding.writing.text.toString())

            val intent = Intent().apply {
                putExtra("todo", todo)
                // MainActivity 로 다시 날려줌
                putExtra("flag", 1)
            }
            setResult(RESULT_OK, intent)
            finish()
        }else{
            backBtnTime=curTime
            Toast.makeText(this,"한번 더 누르면 목록으로 이동합니다.",Toast.LENGTH_SHORT).show()

            binding.writing.isCursorVisible=false
        }
    }
}