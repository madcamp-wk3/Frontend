package com.example.madcamp_wk3.ui.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_wk3.R

data class ChatMessage(
    val sender: String,
    val message: String,
    val isTyping: Boolean = false,
    val suggestions: List<String> = emptyList()
)

class ChatAdapter(private val onSuggestionClick: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    fun addMessage(sender: String, message: String, isTyping: Boolean = false, suggestions: List<String> = emptyList()) {
        messages.add(ChatMessage(sender, message, isTyping, suggestions))
        notifyItemInserted(messages.size - 1)
    }

    fun removeTypingIndicator() {
        val typingIndex = messages.indexOfFirst { it.isTyping }
        if (typingIndex != -1) {
            messages.removeAt(typingIndex)
            notifyItemRemoved(typingIndex)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return when {
            message.isTyping -> 2 // Typing Animation View
            message.sender == "AI" -> 1 // AI View
            else -> 0 // User View
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_ai, parent, false)
                ChatbotViewHolder(view, onSuggestionClick)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_typing, parent, false)
                TypingViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_user, parent, false)
                UserViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is ChatbotViewHolder -> holder.bind(message)
            is TypingViewHolder -> holder.bind()
            is UserViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class ChatbotViewHolder(itemView: View, private val onSuggestionClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val chatbotIcon: ImageView? = itemView.findViewById(R.id.chatbotIcon)
        private val messageTextView: TextView? = itemView.findViewById(R.id.messageTextView)
        private val suggestionContainer: LinearLayout? = itemView.findViewById(R.id.suggestionButtonsContainer)

        init {
            Log.d("ChatbotViewHolder", "✅ ViewHolder Created")

            if (chatbotIcon == null) Log.e("ChatbotViewHolder", "❌ chatbotIcon is NULL!")
            if (messageTextView == null) Log.e("ChatbotViewHolder", "❌ messageTextView is NULL!")
            if (suggestionContainer == null) Log.e("ChatbotViewHolder", "❌ suggestionButtonsContainer is NULL!")
        }

        fun bind(chatMessage: ChatMessage) {
            Log.d("ChatbotViewHolder", "🔹 Binding message: ${chatMessage.message}")

            // Ensure views are not null before using them
            messageTextView?.text = chatMessage.message
            chatbotIcon?.setImageResource(R.drawable.ic_chatbot) // Set chatbot icon

            // Log suggestion buttons
            suggestionContainer?.removeAllViews()
            chatMessage.suggestions.forEach { suggestion ->
                Log.d("ChatbotViewHolder", "🔹 Adding suggestion button: $suggestion")
                val button = Button(itemView.context).apply {
                    text = suggestion
                    setOnClickListener { onSuggestionClick(suggestion) }
                }
                suggestionContainer?.addView(button)
            }
        }
    }

    class TypingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typingIndicator: ImageView = itemView.findViewById(R.id.typingIndicator)
        fun bind() {
            typingIndicator.setImageResource(R.drawable.typing_animation) // Set animated typing dots
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userIcon: ImageView = itemView.findViewById(R.id.userIcon)
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

        fun bind(chatMessage: ChatMessage) {
            messageTextView.text = chatMessage.message
            userIcon.setImageResource(R.drawable.ic_user) // Set user profile icon
        }
    }
}