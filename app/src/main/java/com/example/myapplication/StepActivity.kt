package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityStepsBinding

class StepActivity: AppCompatActivity() {
    lateinit var binding: ActivityStepsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.today.setOnClickListener {
            startActivity(Intent(this,TodayActivity::class.java))
        }
//        binding.tomorrow.setOnClickListener {
//            startActivity(Intent(this,TomorrowActivity::class.java))
//        }
//        binding.someday.setOnClickListener {
//            startActivity(Intent(this,SomedayActivity::class.java))
//        }
    }
}