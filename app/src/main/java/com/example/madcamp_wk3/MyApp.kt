package com.example.madcamp_wk3

import android.app.Application
import com.example.madcamp_wk3.utils.JwtUtils

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 앱 실행 시 JWT 초기화
        JwtUtils.logout(this)
    }
}