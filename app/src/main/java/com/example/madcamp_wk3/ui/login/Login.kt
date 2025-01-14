package com.example.madcamp_wk3.ui.login

class Login {
    data class LoginRequest(
        val username : String,
        val password : String )

    data class LoginResponse(
        val message : String,
        val username : String
    )
}

