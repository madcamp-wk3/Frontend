package com.example.madcamp_wk3.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_wk3.R
import com.example.madcamp_wk3.util.NewsItem

class InnerRecyclerviewAdapter(private val itemList: List<NewsItem> = emptyList()) :
    RecyclerView.Adapter<InnerRecyclerviewAdapter.InnerViewHolder>() {

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.NewsTitle)
        //val imageView : ImageView = itemView.findViewById(R.id.NewsImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_inner_recyclerview_item, parent, false)
        return InnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val news = itemList[position]
        holder.textView.text = news.title
//        Glide.with(holder.imageView.context)
//            .load(news.imageUrl)
//            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
