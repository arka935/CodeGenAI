package com.codegenai.app

import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codegenai.app.databinding.ActivityChatBinding
import com.codegenai.app.models.ChatMessage
import com.codegenai.app.adapters.ChatAdapter
import com.codegenai.app.api.AIProvider
import com.codegenai.app.utils.ProjectManager
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var aiProvider: AIProvider
    private lateinit var projectManager: ProjectManager
    private var selectedModel: String = "openai"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedModel = intent.getStringExtra("model") ?: "openai"
        projectManager = ProjectManager(this)
        aiProvider = AIProvider(selectedModel, this)

        setupToolbar()
        setupRecyclerView()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat with $selectedModel"
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages) { message, action ->
            handleMessageAction(message, action)
        }
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }
    }

    private fun setupListeners() {
        binding.btnSend.setOnClickListener {
            val message = binding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
            }
        }
    }

    private fun sendMessage(message: String) {
        // Add user message
        val userMessage = ChatMessage(
            text = message,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )
        messages.add(userMessage)
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
        binding.messageInput.text.clear()

        // Get AI response
        lifecycleScope.launch {
            try {
                val response = aiProvider.generateCode(message)
                val aiMessage = ChatMessage(
                    text = response,
                    isUser = false,
                    timestamp = System.currentTimeMillis(),
                    isCode = true
                )
                messages.add(aiMessage)
                chatAdapter.notifyItemInserted(messages.size - 1)
                binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
            } catch (e: Exception) {
                Toast.makeText(this@ChatActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleMessageAction(message: ChatMessage, action: String) {
        when (action) {
            "copy" -> copyToClipboard(message.text)
            "download" -> downloadProject(message.text)
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = android.content.ClipData.newPlainText("Code", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun downloadProject(code: String) {
        lifecycleScope.launch {
            try {
                val projectName = "Project_${System.currentTimeMillis()}"
                val zipPath = projectManager.createProjectZip(projectName, code)
                Toast.makeText(this@ChatActivity, "Project saved to: $zipPath", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this@ChatActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
