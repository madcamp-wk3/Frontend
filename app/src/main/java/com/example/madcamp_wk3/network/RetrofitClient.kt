package com.example.madcamp_wk3.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
object RetrofitClient {
    private const val BASE_URL = "https://fastapi-app-313452959284.us-central1.run.app/" // Change to your backend URL

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON Parsing
            .build()
    }
}