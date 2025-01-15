package com.example.madcamp_wk3

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.madcamp_wk3.databinding.ActivityMainBinding
import com.example.madcamp_wk3.ui.dashboard.DashboardFragment
import com.example.madcamp_wk3.ui.home.HomeFragment
import com.example.madcamp_wk3.ui.login.LoginActivity
import com.example.madcamp_wk3.ui.login.MyPageActivity
import com.example.madcamp_wk3.utils.JwtUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navigation_dashboard -> {
                    replaceFragment(DashboardFragment())
                    true
                }
                R.id.navigation_notifications -> {
                    // JWT 검증 후 마이페이지 표시
                    if (JwtUtils.isLoggedIn(this)) {
                        startActivity(Intent(this, MyPageActivity::class.java))
                    } else {
                        // 로그인 페이지로 이동
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragment)
            .commit()
    }
}