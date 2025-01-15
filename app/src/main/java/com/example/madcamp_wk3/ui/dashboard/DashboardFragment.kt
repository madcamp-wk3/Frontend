package com.example.madcamp_wk3.ui.dashboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_wk3.databinding.FragmentDashboardBinding
import com.example.madcamp_wk3.network.GeminiChatManager
import com.example.madcamp_wk3.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        chatAdapter = ChatAdapter { suggestion ->
            binding.chatInput.setText(suggestion)
        }

        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        fetchSingleNews()

        binding.chatInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.sendButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.sendButton.setOnClickListener {
            val userMessage = binding.chatInput.text.toString()
            if (userMessage.isNotBlank()) {
                chatAdapter.addMessage("User", userMessage)
                binding.chatInput.text.clear()
                fetchAIResponse(userMessage)
            }
        }

        return view
    }

    private fun fetchSingleNews() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getSingleNews().execute()
                if (response.isSuccessful && response.body() != null) {
                    val news = response.body()!!

                    withContext(Dispatchers.Main) {
                        val newsMessage = "**Latest News**: ${news.title}\n\n${news.summary}"
                        chatAdapter.addMessage("News", newsMessage)
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatbotFragment", "❌ API Error: ${e.localizedMessage}")
            }
        }
    }

    private fun fetchAIResponse(userMessage: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                chatAdapter.addMessage("AI", "...", isTyping = true)
            }

            delay(1500)

            val aiResponse = GeminiChatManager.getNewsAndChat()

            val exampleReplies = listOf("Tell me more.", "How does this affect investors?", "What should I do next?")

            withContext(Dispatchers.Main) {
                chatAdapter.removeTypingIndicator()
                chatAdapter.addMessage("AI", aiResponse, false, exampleReplies)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}