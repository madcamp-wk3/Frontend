package com.example.madcamp_wk3.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.madcamp_wk3.network.ApiService
import com.example.madcamp_wk3.network.NewsResponse
import com.example.madcamp_wk3.network.RetrofitClient
import com.example.madcamp_wk3.util.NewsItem
import com.example.madcamp_wk3.util.Section
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    private val _newsSections = MutableLiveData<List<Section>>()
    val newsSections: LiveData<List<Section>> get() = _newsSections
    private val fixedCategories = listOf("경기", "정치", "사회", "경제", "국제", "과학")
    fun fetchNews() {
        Log.d("DashboardViewModel", "📡 Initiating API Call to fetch news...")

        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.getNews().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {


                    val sections = response.body()?.sections ?: emptyList()
                    _newsSections.postValue(sections)

                    Log.d("DashboardViewModel", "✅ API Call Success. Received ${sections.size} sections")
                    sections.forEachIndexed { index, section ->
                        Log.d("DashboardViewModel", "📌 Section[$index]: ${section.title} (contains ${section.newsItems.size} articles)")
                    }
                } else {
                    Log.e("DashboardViewModel", "❌ API Response Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("DashboardViewModel", "❌ API Call Error: ${t.message}")
            }
        })
    }
}