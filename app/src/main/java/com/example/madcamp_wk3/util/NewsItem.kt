package com.example.madcamp_wk3.util

data class NewsItem(
    val title: String,// News headline
    val news_url: String,   // URL to the full article
    val category: String,   // News category (e.g., economy, politics)
    val summary: String,    // Short news description (replace `content`)

    val imageUrl: String
)