
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
import com.example.madcamp_wk3.network.ApiService
import com.example.madcamp_wk3.network.LoginResponse
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.madcamp_wk3.network.TokenRequest

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val binding get() = _binding!!

    // ✅ Retrofit API Service
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") // Emulator: Use this instead of 127.0.0.1
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // ✅ Initialize Google Sign-In
        oneTapClient = Identity.getSignInClient(requireContext())

        // ✅ ActivityResultLauncher setup
        signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    handleSignInSuccess(credential)
                } catch (e: Exception) {
                    Log.e("GoogleSignIn", "❌ Login Failed: ${e.message}")
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("GoogleSignIn", "❌ Google Login Canceled")
                Toast.makeText(context, "Google Login Canceled", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Google Login Button Click
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
                    .setServerClientId(getString(R.string.server_client_id)) // OAuth Client ID
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
                    Log.e("GoogleSignIn", "❌ Login Start Failed: ${e.message}")
                    Toast.makeText(context, "Login Start Failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("GoogleSignIn", "❌ Login Request Failed: ${e.message}")
                Toast.makeText(context, "Login Request Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleSignInSuccess(credential: SignInCredential) {
        val idToken = credential.googleIdToken
        val email = credential.id
        val displayName = credential.displayName

        Log.d("GoogleSignIn", "✅ Login Success: $displayName, $email")

        if (idToken != null) {
            Log.d("GoogleSignIn", "📩 Sending ID Token to Backend: $idToken")
            sendIdTokenToBackend(idToken)
        } else {
            Log.e("GoogleSignIn", "❌ ID Token is NULL")
        }
    }

    private fun sendIdTokenToBackend(idToken: String) {
        val request = TokenRequest(idToken)

        apiService.googleLogin(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("GoogleSignIn", "✅ Server Response: ${loginResponse?.message}")
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("GoogleSignIn", "❌ Server Error: $errorBody")
                    Toast.makeText(context, "Server Error: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("GoogleSignIn", "❌ Network Error: ${t.message}")
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}