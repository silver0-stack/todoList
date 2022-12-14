package com.example.myapplication.config

import android.app.Application
import com.example.myapplication.repository.TodoRepository


/*앱이 실행될 때 단 한번만 실행되도록 하기 위해 작성했다
* 앱 실행과 동시에 Repository 초기화를 통해 데이터베이스가 없을 경우 새로 빌드하도록 함*/
class ApplicationClass:Application() {
    override fun onCreate() {
        super.onCreate()

        TodoRepository.initialize(this)

    }
}