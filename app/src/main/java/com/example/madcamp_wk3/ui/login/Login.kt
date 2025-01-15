package com.example.madcamp_wk3.ui.login

class Login {
    data class LoginRequest(
        val username : String,
        val password : String )

    data class LoginResponse(
        val message : String,
        val username : String
    )

    data class GoogleRequest (
        val id_token : String
    )

    data class GoogleResponse (
        val id_token : String
    )

    data class SignupRequest(
        val username: String,
        val password : String
    )

    data class SignupResponse(
        val message: String,
        val user_id : String
    )
}

