package com.example.madcamp_wk3.network

import android.util.Log

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

object GeminiChatManager {
    private const val API_KEY = "AIzaSyC_A6jQF_OumelFBa3ij6Ftt969PvZCsos" // Replace with your actual API key

    suspend fun getNewsAndChat(): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getSingleNews().execute()
                if (!response.isSuccessful || response.body() == null) {
                    return@withContext "No news available at the moment."
                }

                // Extract the single title and summary
                val news = response.body()!!
                val newsTitle = news.title
                val newsSummary = news.summary

                // Prepare input for Gemini
                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash-001",
                    apiKey = API_KEY
                )

                val inputContent = content {
                    text("""
                    You are an economic analyst. Provide insights based on this news:

                    **Title**: $newsTitle
                    **Summary**: $newsSummary

                    - Explain its economic impact.
                    - Suggest strategies for investors.
                    - Keep it short (2-3 sentences).
                    -say it in korean
                """.trimIndent())
                }

                val aiResponse = generativeModel.generateContent(inputContent).text
                    ?: "I couldn't generate an analysis at this time."

                Log.d("GeminiChatManager", "✅ AI Response: $aiResponse")
                return@withContext aiResponse
            } catch (e: Exception) {
                Log.e("GeminiChatManager", "❌ API Error: ${e.localizedMessage}")
                return@withContext "Couldn't fetch news or generate an analysis."
            }
        }
    }
}