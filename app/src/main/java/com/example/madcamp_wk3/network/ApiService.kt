package com.example.madcamp_wk3.network

import com.example.madcamp_wk3.util.Section
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/news") // Replace with your actual API endpoint
    fun getNewsSections(): Call<List<Section>>
    @GET("/users/news")  // Your new API endpoint
    fun getSingleNews(): Call<NewsResponse>

}