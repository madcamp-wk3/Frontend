package com.example.madcamp_wk3.network

import retrofit2.Call
import retrofit2.http.GET

data class NewsResponse(
    val title: String,      // News headline
    val news_url: String,   // URL to the full article
    val category: String,   // News category (e.g., economy, politics)
    val summary: String,    // Short news description (replace `content`)
    val likes: List<String> // List of likes (if needed)
)

interface NewsApiService {
    @GET("/news")  // Replace with your actual API endpoint
    fun getNews(): Call<List<NewsResponse>>
}