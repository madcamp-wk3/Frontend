package com.example.madcamp_wk3.ui.dashboard

import NewsNode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _newsArticles = MutableLiveData<List<NewsNode>>().apply {
        value = listOf(
            NewsNode("Tech"),
            NewsNode("AI"),
            NewsNode("Stocks"),
            NewsNode("Policy"),
            NewsNode("Health"),
            NewsNode("Crypto")
        )
    }

    val newsArticles: LiveData<List<NewsNode>> = _newsArticles
}