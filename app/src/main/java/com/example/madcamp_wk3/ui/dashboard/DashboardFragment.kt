package com.example.madcamp_wk3.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.madcamp_wk3.util.Section

class DashboardFragment : Fragment() {
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        viewModel.fetchNews()

        return ComposeView(requireContext()).apply {
            setContent {
                val sections by viewModel.newsSections.observeAsState(initial = emptyList())
                DashboardScreen(sections)
            }
        }
    }
}

@Composable
fun DashboardScreen(sections: List<Section>) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (sections.isEmpty()) {
            Text("Loading...", Modifier.padding(16.dp))
        } else {
            sections.forEach { section ->
                Text("Section: ${section.title}")
            }
        }
    }
}