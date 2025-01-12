package com.example.madcamp_wk3.ui.dashboard

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_wk3.databinding.FragmentDashboardBinding
import com.google.gson.Gson

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val webView : WebView = binding.webView
        setUpWebView(webView)

        // local html 파일 로드 - fragment위에 올리는 올리는 정적인 페이지
        webView.loadUrl("file:///android_asset/network_graph.html")


        val nodes = generateNodes(10) // 노드 10개 생성
        val links = generateLinks(nodes)

        updateGraph(webView, nodes, links)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpWebView(webView: WebView) {
        webView.webViewClient = WebViewClient() // WebView 내부 링크 처리
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // JavaScript 활성화
    }


    private fun updateGraph(webView: WebView,
                            nodes: List<Map<String, Any>>,
                            links: List<Map<String, String>>) {
        val jsonNodes = Gson().toJson(nodes)
        val jsonLinks = Gson().toJson(links)
        webView.evaluateJavascript("updateGraph($jsonNodes, $jsonLinks)", null)

        Log.d("WebView", "Nodes: $jsonNodes")
        Log.d("WebView", "Links: $jsonLinks")


    }

    private fun generateNodes(count: Int): List<Map<String, Any>> {
        // 동적 노드 데이터 생성
        return List(count) { index ->
            mapOf(
                "id" to "Node $index",
                "size" to (5 + index * 2) // 크기를 다르게 설정
            )
        }
    }

    private fun generateLinks(nodes: List<Map<String, Any>>): List<Map<String, String>> {
        // 동적 링크 데이터 생성 (노드 간 연결)
        return nodes.windowed(2, 1) { pair ->
            mapOf(
                "source" to pair[0]["id"].toString(),
                "target" to pair[1]["id"].toString()
            )
        }
    }

}