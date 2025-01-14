package com.example.madcamp_wk3.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madcamp_wk3.databinding.FragmentLoginSignupBinding
import com.example.madcamp_wk3.network.RetrofitClient
import retrofit2.Call

class SignupFragment : Fragment(){
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var signupBtn : Button
    private lateinit var newUserName : String
    private lateinit var newUserPw : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginSignupBinding.inflate(inflater, container, false)
        username = binding.signUpName
        password = binding.signUpPw
        signupBtn = binding.signUp
        val root = binding.root

        signupBtn.setOnClickListener{
            signup()
        }

        return root
    }

    private fun signup(){
        newUserName = username.text.toString()
        newUserPw = password.text.toString()
        Log.d("LoginService heeju", "${newUserName} and ${newUserPw} get")

        // 요청 데이터 생성
        val signupRequest = Login.SignupRequest(username = newUserName, password = newUserPw)

        // Retrofit API 호출
        RetrofitClient.signupApi.signup(signupRequest).enqueue(object : retrofit2.Callback<Login.SignupResponse> {
            override fun onResponse(call: Call<Login.SignupResponse>, response: retrofit2.Response<Login.SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show()
                    Log.d("LoginService heeju", "Response: ${signupResponse?.message}")
                } else {
                    Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    Log.e("SignupResponse heeju", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Login.SignupResponse>, t: Throwable) {
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("SignupResponse", "Failure: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}