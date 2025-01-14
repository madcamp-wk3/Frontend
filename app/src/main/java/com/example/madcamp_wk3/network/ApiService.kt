package com.example.madcamp_wk3.network

import com.example.madcamp_wk3.ui.login.Login
import com.example.madcamp_wk3.util.Section
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/news") // Replace with your actual API endpoint
    fun getNewsSections(): Call<List<Section>>

    @POST("/login")
    fun login(@Body request: Login.LoginRequest): Call<Login.LoginResponse>
}