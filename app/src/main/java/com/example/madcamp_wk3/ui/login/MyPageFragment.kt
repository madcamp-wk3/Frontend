package com.example.madcamp_wk3.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madcamp_wk3.databinding.MyPageBinding
import com.example.madcamp_wk3.utils.JwtUtils

class MyPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MyPageBinding.inflate(inflater, container,false)
        val root = binding.root
        return root

        binding.logoutBtn.setOnClickListener {
            //logout()
        }
    }

//    private fun logout(){
//        JwtUtils.logout(requireContext())
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//        finish() // 현재 Activity 종료
//    }
}