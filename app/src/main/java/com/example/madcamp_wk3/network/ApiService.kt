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
    @GET("/users/news")  // Your new API endpoint
    fun getSingleNews(): Call<NewsResponse>


    @POST("/login")
    fun login(@Body request: Login.LoginRequest): Call<Login.LoginResponse>

    @POST("/login/google")
    fun loginGoogle(@Body request : Login.GoogleRequest) : Call<Login.GoogleResponse>

    @POST("/signIN")
    fun signup(@Body requst : Login.SignupRequest) : Call<Login.SignupResponse>

}