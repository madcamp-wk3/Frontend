package com.example.madcamp_wk3.ui.notifications

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_wk3.R
import com.example.madcamp_wk3.databinding.FragmentHomeBinding
import com.example.madcamp_wk3.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import org.json.JSONObject

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>

    private lateinit var loginId : EditText
    private lateinit var loginPw : EditText

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Google Identity Services 초기화
        oneTapClient = Identity.getSignInClient(requireContext())

        // ActivityResultLauncher 초기화
        signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken
                    Toast.makeText(context, "idToken is ${idToken}", Toast.LENGTH_LONG).show()
                    handleSignInSuccess(credential)
                } catch (e: Exception) {
                    Log.e("GoogleSignIn", "로그인 실패: ${e.message}")
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Google 로그인 취소", Toast.LENGTH_SHORT).show()
            }
        }

        // Google 로그인 버튼 클릭 이벤트
        binding.btnGoogleSignIn.setOnClickListener {
            startGoogleSignIn()
        }

        binding.loginBtn.setOnClickListener{
            loginService()
        }
        return root
    }

    private fun startGoogleSignIn() {
        // 저장된 사용자 인증정보로 사용자 로그인 처리 페이지 oneTabLogin을 앱에 통합
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("1007643601883-kf6s28beaotuv83d3bs61hkufomo73jc.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            //.setAutoSelectEnabled(false)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                    signInLauncher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("GoogleSignIn", "로그인 시작 실패: ${e.message}")
                    Toast.makeText(context, "로그인 시작 실패", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("GoogleSignIn", "로그인 요청 실패: ${e.localizedMessage}")
                Toast.makeText(context, "로그인 요청 실패: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleSignInSuccess(credential: SignInCredential) {
        val idToken = credential.googleIdToken
        val email = credential.id
        val displayName = credential.displayName

        if (idToken != null) {
            Log.d("GoogleSignIn", "로그인 성공 - ID Token: $idToken")
            sendTokenToServer(idToken) // ID 토큰을 서버로 전송
        } else {
            Log.e("GoogleSignIn", "ID Token이 없습니다.")
            Toast.makeText(context, "ID Token을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "환영합니다, $displayName!", Toast.LENGTH_SHORT).show()
    }

    private fun sendTokenToServer(idToken: String) {
        val url = "http://127.0.0.1:8000/login/google" // FastAPI 서버 주소
        val requestBody = JSONObject()
        requestBody.put("id_token", idToken)

        // Volley 요청 생성
        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            { response ->
                Log.d("ServerResponse", "서버 응답: $response")
                Toast.makeText(context, "서버 응답: $response", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Log.e("ServerError", "에러: ${error.message}")
                Toast.makeText(context, "서버 요청 실패", Toast.LENGTH_SHORT).show()
            }
        )
        // 요청 큐에 추가
        requestQueue.add(jsonObjectRequest)
    }

    private fun loginService() {
        // EditText에서 입력값 가져오기
        val username = loginId.text.toString()
        val password = loginPw.text.toString()

        // 서버 URL
        val url = "http://127.0.0.1:8000/login" // FastAPI 서버 주소
        val requestBody = JSONObject()
        requestBody.put("username", username)
        requestBody.put("password", password)

        // 요청 생성
        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            { response ->
                // 로그인 성공 시 처리
                val message = response.getString("message")
                val user = response.getString("username")
                Toast.makeText(context, "Welcome, $user!", Toast.LENGTH_SHORT).show()
                Log.d("LoginService", "Response: $message")
            },
            { error ->
                // 로그인 실패 시 처리
                val errorMessage = error.networkResponse?.statusCode?.let {
                    when (it) {
                        401 -> "Invalid username or password"
                        else -> "Server error"
                    }
                } ?: "Network error"
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e("LoginService", "Error: ${error.message}")
            }
        )

        // 요청 큐에 추가
        requestQueue.add(jsonObjectRequest)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        try {
            oneTapClient.signOut() // 명시적으로 로그아웃 처리
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "클라이언트 종료 중 오류: ${e.message}")
        }
    }
}


