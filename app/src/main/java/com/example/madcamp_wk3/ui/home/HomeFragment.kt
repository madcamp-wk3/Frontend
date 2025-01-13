package com.example.madcamp_wk3.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.madcamp_wk3.databinding.FragmentHomeBinding
import com.example.madcamp_wk3.util.NewsItem
import com.example.madcamp_wk3.util.Section
import com.example.madcamp_wk3.util.adapter.OuterRecyclerviewAdapter

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


        // 더미 데이터 추가
        initializeDummyData()

        // RecyclerView 설정
        binding.outerRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        outerAdapter = OuterRecyclerviewAdapter(sectionList)
        binding.outerRecyclerview.adapter = outerAdapter

        // 추가 버튼 설정 (예: 새로운 섹션 추가)
        binding.outerRecyclerviewAddBtn.setOnClickListener {
            val newItems = mutableListOf(
                NewsItem("새로운 뉴스1", "https://via.placeholder.com/150"),
                NewsItem("새로운 뉴스2", "https://via.placeholder.com/150")
            )
            val newSection = Section("새로운 섹션", newItems)
            sectionList.add(newSection)
            outerAdapter.notifyItemInserted(sectionList.size - 1)
        }
        return root

    }




    private fun initializeDummyData() {
        val section1Items = mutableListOf(
            NewsItem("오늘의 뉴스1", "https://imgnews.pstatic.net/image/119/2025/01/12/0002912902_001_20250112145009956.jpeg?type=w860"),
            NewsItem("오늘의 뉴스2", "https://imgnews.pstatic.net/image/119/2025/01/12/0002912902_001_20250112145009956.jpeg?type=w860"),
            NewsItem("오늘의 뉴스3", "https://imgnews.pstatic.net/image/119/2025/01/12/0002912902_001_20250112145009956.jpeg?type=w860")
        )

        val section2Items = mutableListOf(
            NewsItem("경제 뉴스1", "https://via.placeholder.com/150"),
            NewsItem("경제 뉴스2", "https://via.placeholder.com/150"),
            NewsItem("경제 뉴스3", "https://via.placeholder.com/150")
        )

        sectionList.add(Section("오늘의 뉴스", section1Items))
        sectionList.add(Section("경제 뉴스", section2Items))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}