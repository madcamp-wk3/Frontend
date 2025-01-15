package com.example.madcamp_wk3.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.madcamp_wk3.databinding.FragmentHomeBinding
import com.example.madcamp_wk3.network.RetrofitClient
import com.example.madcamp_wk3.util.NewsItem
import com.example.madcamp_wk3.util.Section
import com.example.madcamp_wk3.util.adapter.OuterRecyclerviewAdapter
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var outerAdapter: OuterRecyclerviewAdapter
    private val sectionList = mutableListOf<Section>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root




        // RecyclerView 설정
        binding.outerRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        outerAdapter = OuterRecyclerviewAdapter(sectionList)
        binding.outerRecyclerview.adapter = outerAdapter

        fetchNewsData()


        // 추가 버튼 설정 (예: 새로운 섹션 추가)
        binding.outerRecyclerviewAddBtn.setOnClickListener {
            val newSection = Section("새로운 섹션", listOf())
            sectionList.add(newSection)
            outerAdapter.notifyItemInserted(sectionList.size - 1)
        }
        return root

    }

    private fun fetchNewsData() {
        RetrofitClient.instance.getNewsSections().enqueue(object : Callback<List<Section>> {
            override fun onResponse(call: Call<List<Section>>, response: Response<List<Section>>) {
                if (response.isSuccessful) {
                    val rawResponse = response.body()

                    // ✅ Log the raw response object
                    Log.d("HomeFragment", "🔍 Raw Response Object: $rawResponse")

                    if (rawResponse == null) {
                        Log.e("HomeFragment", "❌ API returned NULL instead of list")
                        return
                    }

                    // ✅ Log JSON response before parsing
                    val rawJson = response.errorBody()?.string()
                    Log.d("HomeFragment", "📜 Full JSON Response: $rawJson")

                    // ✅ Check how the news items are structured
                    rawResponse.forEachIndexed { index, section ->
                        Log.d("HomeFragment", "📰 Section [$index]: ${section.title}")

                        section.newsItems?.forEachIndexed { newsIndex, newsItem ->
                            Log.d("HomeFragment", "  🗞️ News [$newsIndex]: ${newsItem.title}")
                            Log.d("HomeFragment", "  📄 Summary: ${newsItem.summary}")
                            Log.d("HomeFragment", "  🖼️ Image URL: ${newsItem.imageUrl ?: "No Image"}")
                        }
                    }

                    sectionList.clear()
                    sectionList.addAll(rawResponse)
                    outerAdapter.notifyDataSetChanged()

                } else {
                    Log.e("HomeFragment", "❌ API Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Section>>, t: Throwable) {
                Log.e("HomeFragment", "❌ Network Error: ${t.message}")
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}