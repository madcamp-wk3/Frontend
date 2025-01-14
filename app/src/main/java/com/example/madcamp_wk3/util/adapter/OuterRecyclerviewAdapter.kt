package com.example.madcamp_wk3.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_wk3.R
import com.example.madcamp_wk3.util.Section

class OuterRecyclerviewAdapter : RecyclerView.Adapter<OuterRecyclerviewAdapter.OuterViewHolder>() {

    private val fixedCategories = listOf("경기", "정치", "사회", "경제", "국제", "과학") // ✅ Six fixed sections


    inner class OuterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionTitle: TextView = itemView.findViewById(R.id.sectionTitle)
        val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.innerRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_outer_recyclerview_item, parent, false)
        return OuterViewHolder(view)
    }

    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
        val category = fixedCategories[position] // ✅ Always use fixed categories

        // Find news items belonging to this category, or use an empty list if not found
        val section = sectionList.find { it.title == category } ?: Section(category, emptyList())

        holder.sectionTitle.text = section.title

        holder.innerRecyclerView.layoutManager = LinearLayoutManager(
            holder.itemView.context, LinearLayoutManager.HORIZONTAL, false
        )
        holder.innerRecyclerView.adapter = InnerRecyclerviewAdapter(section.newsItems)
    }

    override fun getItemCount(): Int {
        return fixedCategories.size // ✅ Always return 6
    }

    // 🔥 Update data without affecting fixed sections

}