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
        if (sectionList.isEmpty()) {
            initializeDummyData()
        }

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
            NewsItem("잘나가던 자동차株…환율 급등, 전기차 캐즘, 트럼프 2기에 긴장", "https://img3.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202501/13/mk/20250113150906097rvvc.png"),
            NewsItem("[특징주] 에스와이, 'LA 산불' 건자재 수요 폭증 예상… 미 화재안전 인증 자재 수출 부각", "https://img2.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202501/13/moneyweek/20250113150119337ncbb.jpg"),
            NewsItem("기준금리 내리자 몰려든 채권 개미…지난해 42.5조 순매수", "https://img4.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202501/13/moneytoday/20250113142151697oskj.jpg"),
            NewsItem("증시 발목잡은 새내기주… 지난해 수익률 ‘-43%’", "https://img1.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202501/13/munhwa/20250113114516253yjdc.jpg")
        )

        val section2Items = mutableListOf(
            NewsItem("젠슨 황 발언에 주가 급락하자 서둘렀나?... 142만주 추가 상장 일주일 앞당긴 양자컴株", "https://img2.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202501/13/chosunbiz/20250113151924332wxpy.jpg"),
            NewsItem("신한은행, 대출 가산금리 최대 0.3%p↓…KB·우리도 가세할 듯", "https://img2.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202501/13/ned/20250113142900354tkkk.jpg"),
            NewsItem("韓 소매판매는 21년 만에 최악...美 연말 소매지출은 5% 증가", "https://img4.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202501/13/fnnewsi/20250113141342564eiug.jpg"),
            NewsItem("'환율, 다시 오른다'…1월 한은 금통위 '동결' 힘받나", "https://img4.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202501/13/newsis/20250113091825412smpr.jpg")
        )

        sectionList.add(Section("주식", section1Items))
        sectionList.add(Section("금융", section2Items))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}