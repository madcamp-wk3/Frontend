package com.example.madcamp_wk3.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_wk3.databinding.FragmentHomeBinding
import com.example.madcamp_wk3.ui.dashboard.DashboardViewModel
import com.example.madcamp_wk3.util.Section
import com.example.madcamp_wk3.util.adapter.OuterRecyclerviewAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var outerAdapter: OuterRecyclerviewAdapter
    private val sectionList = mutableListOf<Section>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
        setupRecyclerView()
        observeNewsData()

        return root
    }

    private fun setupRecyclerView() {
        binding.outerRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        outerAdapter = OuterRecyclerviewAdapter(sectionList)
        binding.outerRecyclerview.adapter = outerAdapter
    }

    private fun observeNewsData() {
        dashboardViewModel.newsSections.observe(viewLifecycleOwner) { sections ->
            sectionList.clear()
            sectionList.addAll(sections)
            outerAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}