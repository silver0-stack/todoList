package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityTodayBinding

class TodayActivity : AppCompatActivity() {
    lateinit var binding: ActivityTodayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            TODO("뒤로 가기 코드")
        }
        binding.todAdd.setOnClickListener{

        }

    }
}