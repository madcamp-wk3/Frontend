package com.example.madcamp_wk3.util

data class NewsItem(
    val title: String,
    val url: String = "https://example.com",
    val category: String,
    val summary: String,
    val imageUrl: String? = null // ✅ Allow null values
)