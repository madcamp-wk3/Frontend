package com.example.madcamp_wk3.ui.dashboard

import NewsGraphScreen
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.observe
import com.example.madcamp_wk3.ui.dashboard.DashboardViewModel


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                DashboardScreen(dashboardViewModel)
            }
        }
    }
}

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val newsArticles by viewModel.newsArticles.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        if (newsArticles.isNotEmpty()) {
            NewsGraphScreen(newsArticles) // ✅ Now correctly passing newsArticles
        } else {
            Text("Loading News...", modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen(viewModel = DashboardViewModel())
}
