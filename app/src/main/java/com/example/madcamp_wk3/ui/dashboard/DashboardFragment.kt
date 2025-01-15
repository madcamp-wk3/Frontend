package com.example.madcamp_wk3.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_wk3.R
import com.example.madcamp_wk3.databinding.FragmentDashboardBinding
import com.example.madcamp_wk3.network.GeminiChatManager
import com.example.madcamp_wk3.network.RetrofitClient
import com.example.madcamp_wk3.ui.dashboard.ChatAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerView = view.findViewById(R.id.chatRecyclerView)
        chatInput = view.findViewById(R.id.chatInput)
        sendButton = view.findViewById(R.id.sendButton)

        // ✅ Attach adapter
        chatAdapter = ChatAdapter()
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Send message on button click
        sendButton.setOnClickListener {
            val userMessage = chatInput.text.toString()
            if (userMessage.isNotBlank()) {
                chatAdapter.addMessage("User", userMessage)
                chatInput.text.clear()
                fetchAIResponse(userMessage)
            }
        }

        return view
    }

    private fun fetchSingleNews() {
        lifecycleScope.launch(Dispatchers.IO) { // ✅ Moves work to background thread
            try {
                val response = RetrofitClient.instance.getSingleNews().execute()
                if (response.isSuccessful && response.body() != null) {
                    val news = response.body()!!

                    withContext(Dispatchers.Main) { // ✅ Switch back to UI thread
                        Log.d("ChatbotFragment", "✅ News: ${news.title} - ${news.summary}")
                    }
                } else {
                    Log.e("ChatbotFragment", "❌ API Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ChatbotFragment", "❌ API Error: ${e.localizedMessage}")
            }
        }
    }


    private fun addMessageToChat(sender: String, message: String) {
        chatAdapter.addMessage(sender, message)
    }

    private fun fetchAIResponse(userMessage: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val aiResponse = GeminiChatManager.getNewsAndChat()
                withContext(Dispatchers.Main) {
                    chatAdapter.addMessage("AI", aiResponse)
                    recyclerView.scrollToPosition(chatAdapter.itemCount - 1) // Auto-scroll
                }
            } catch (e: Exception) {
                Log.e("ChatbotFragment", "❌ API Error: ${e.localizedMessage}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}