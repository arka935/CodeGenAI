package com.codegenai.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codegenai.app.databinding.ActivityModelSelectionBinding

class ModelSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModelSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModelSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupModelButtons()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Select AI Model"
    }

    private fun setupModelButtons() {
        binding.btnOpenAI.setOnClickListener { selectModel("openai") }
        binding.btnGemini.setOnClickListener { selectModel("gemini") }
        binding.btnGrok.setOnClickListener { selectModel("grok") }
        binding.btnGroq.setOnClickListener { selectModel("groq") }
        binding.btnQwen.setOnClickListener { selectModel("qwen") }
        binding.btnDeepSeek.setOnClickListener { selectModel("deepseek") }
        binding.btnCopilot.setOnClickListener { selectModel("copilot") }
    }

    private fun selectModel(modelName: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("model", modelName)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
