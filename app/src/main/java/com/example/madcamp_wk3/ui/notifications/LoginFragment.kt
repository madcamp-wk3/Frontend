//package com.example.madcamp_wk3.ui.notifications
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.madcamp_wk3.databinding.FragmentLoginBinding
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable
//
//class LoginFragment : Fragment() {
//
//    private var _binding: FragmentLoginBinding? = null
//    private lateinit var googleSignInClient: GoogleSignInClient
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val notificationsViewModel =
//            ViewModelProvider(this).get(NotificationsViewModel::class.java)
//
//        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail() // 이메일 요청
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        val root: View = binding.root
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

package com.example.madcamp_wk3.ui.notifications

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_wk3.R
import com.example.madcamp_wk3.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>

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

        return root
    }

    private fun startGoogleSignIn() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.server_client_id)) // OAuth 클라이언트 ID
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(false)
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
                Log.e("GoogleSignIn", "로그인 요청 실패: ${e.message}")
                Toast.makeText(context, "로그인 요청 실패", Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleSignInSuccess(credential: SignInCredential) {
        val email = credential.id
        val displayName = credential.displayName
        Log.d("GoogleSignIn", "로그인 성공: $displayName, $email")
        Toast.makeText(context, "환영합니다, $displayName!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
