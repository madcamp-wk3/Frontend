package com.example.madcamp_wk3.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_wk3.R
import com.example.madcamp_wk3.util.Section

class OuterRecyclerviewAdapter(private val sectionList: MutableList<Section>) :
    RecyclerView.Adapter<OuterRecyclerviewAdapter.OuterViewHolder>() {

    inner class OuterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionTitle : TextView = itemView.findViewById(R.id.sectionTitle)
        val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.innerRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_outer_recyclerview_item, parent, false)
        return OuterViewHolder(view)
    }

    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {

        val section = sectionList[position]
        holder.sectionTitle.text = section.section_title
        // LayoutManager 중복 생성 방지
        //if (holder.outerRecyclerview.layoutManager == null) {
        holder.innerRecyclerView.layoutManager = LinearLayoutManager(
                holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.innerRecyclerView.adapter=InnerRecyclerviewAdapter(section.items)


        //holder.outerRecyclerview.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)



    }

    override fun getItemCount(): Int {
        return sectionList.size
    }
}
