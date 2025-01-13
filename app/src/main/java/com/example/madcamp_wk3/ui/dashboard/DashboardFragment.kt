package com.example.madcamp_wk3.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_wk3.databinding.FragmentDashboardBinding
import com.google.gson.Gson

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val webView: WebView = binding.webView
        setUpWebView(webView)

        // Load the local HTML file
        webView.loadUrl("file:///android_asset/network_graph.html")

        // Generate dynamic nodes and links
        val nodes = generateNodes(10) // Generate 10 nodes
        val links = generateLinks(nodes)

        // Delay sending data to WebView (wait for HTML to load)
        webView.postDelayed({
            updateGraph(webView, nodes, links)
        }, 2000) // 2 seconds delay to ensure WebView is fully loaded

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpWebView(webView: WebView) {
        webView.webViewClient = WebViewClient() // WebView internal links handling
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript
    }

    private fun updateGraph(webView: WebView, nodes: List<Map<String, Any>>, links: List<Map<String, String>>) {
        val jsonNodes = Gson().toJson(nodes)
        val jsonLinks = Gson().toJson(links)

        val jsCommand = "updateGraph($jsonNodes, $jsonLinks);"
        webView.evaluateJavascript(jsCommand, null)

        Log.d("WebView", "Nodes: $jsonNodes")
        Log.d("WebView", "Links: $jsonLinks")
    }

    private fun generateNodes(count: Int): List<Map<String, Any>> {
        return List(count) { index ->
            mapOf(
                "id" to "Node $index",
                "size" to (5 + index * 2) // Dynamic size
            )
        }
    }

    private fun generateLinks(nodes: List<Map<String, Any>>): List<Map<String, String>> {
        return nodes.windowed(2, 1) { pair ->
            mapOf(
                "source" to pair[0]["id"].toString(),
                "target" to pair[1]["id"].toString()
            )
        }
    }
}