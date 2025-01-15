package com.example.madcamp_wk3.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp_wk3.databinding.MyPageBinding
import com.example.madcamp_wk3.utils.JwtUtils

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutBtn.setOnClickListener {
            logout()
        }


        val username = JwtUtils.getUsername(this) // 저장된 username 가져오기
        binding.userName.text = "$username 님 안녕하세요:D!"
    }

    private fun logout(){
        JwtUtils.logout(this)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // 현재 Activity 종료
    }
}