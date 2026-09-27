package com.codegenai.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codegenai.app.databinding.ChatMessageItemBinding
import com.codegenai.app.models.ChatMessage

class ChatAdapter(
    private val messages: List<ChatMessage>,
    private val onAction: (ChatMessage, String) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatMessageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatViewHolder(binding, onAction)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    class ChatViewHolder(
        private val binding: ChatMessageItemBinding,
        private val onAction: (ChatMessage, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            if (message.isUser) {
                binding.userMessageContainer.visibility = android.view.View.VISIBLE
                binding.aiMessageContainer.visibility = android.view.View.GONE
                binding.userMessageText.text = message.text
            } else {
                binding.userMessageContainer.visibility = android.view.View.GONE
                binding.aiMessageContainer.visibility = android.view.View.VISIBLE
                binding.aiMessageText.text = message.text

                if (message.isCode) {
                    binding.codeContainer.visibility = android.view.View.VISIBLE
                    binding.btnCopyCode.setOnClickListener {
                        onAction(message, "copy")
                    }
                    binding.btnDownloadZip.setOnClickListener {
                        onAction(message, "download")
                    }
                } else {
                    binding.codeContainer.visibility = android.view.View.GONE
                }
            }
        }
    }
}
