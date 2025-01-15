package com.example.madcamp_wk3.util.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_wk3.R
import com.example.madcamp_wk3.util.NewsItem

class InnerRecyclerviewAdapter(private val itemList: List<NewsItem> = emptyList()) :
    RecyclerView.Adapter<InnerRecyclerviewAdapter.InnerViewHolder>() {

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.NewsTitle)
        val imageView : ImageView = itemView.findViewById(R.id.NewsImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_inner_recyclerview_item, parent, false)
        return InnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val news = itemList[position]
        holder.textView.text = news.title
        Glide.with(holder.imageView.context)
            .load(news.imageUrl)
            .into(holder.imageView)

        // Log the news_url to check if it's present
        val context = holder.itemView.context
        if (news.news_url.isNullOrEmpty()) {
            Log.e("InnerRecyclerviewAdapter", "Missing news_url for news item at position $position")
        } else {
            Log.d("InnerRecyclerviewAdapter", "News URL for position $position: ${news.news_url}")
        }

        holder.itemView.setOnClickListener {
            if (!news.news_url.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(news.news_url)
                }
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "URL not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
