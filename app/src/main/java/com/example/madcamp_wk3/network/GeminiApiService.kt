package com.example.madcamp_wk3.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class GeminiRequest(val contents: List<Map<String, String>>)
data class GeminiResponse(val candidates: List<Map<String, String>>)

interface GeminiApiService {
    @Headers("Content-Type: application/json")
    @POST("https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateText")
    fun getResponse(@Body request: GeminiRequest): Call<GeminiResponse>
}