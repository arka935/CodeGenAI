package com.codegenai.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegenai.app.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        loadSettings()
    }

    private fun loadSettings() {
        val sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE)
        
        binding.etOpenAIKey.setText(sharedPreferences.getString("openai_key", ""))
        binding.etGeminiKey.setText(sharedPreferences.getString("gemini_key", ""))
        binding.etGrokKey.setText(sharedPreferences.getString("grok_key", ""))
        binding.etGroqKey.setText(sharedPreferences.getString("groq_key", ""))
        binding.etQwenKey.setText(sharedPreferences.getString("qwen_key", ""))
        binding.etDeepSeekKey.setText(sharedPreferences.getString("deepseek_key", ""))
        binding.etCopilotKey.setText(sharedPreferences.getString("copilot_key", ""))

        binding.btnSaveSettings.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("openai_key", binding.etOpenAIKey.text.toString())
            editor.putString("gemini_key", binding.etGeminiKey.text.toString())
            editor.putString("grok_key", binding.etGrokKey.text.toString())
            editor.putString("groq_key", binding.etGroqKey.text.toString())
            editor.putString("qwen_key", binding.etQwenKey.text.toString())
            editor.putString("deepseek_key", binding.etDeepSeekKey.text.toString())
            editor.putString("copilot_key", binding.etCopilotKey.text.toString())
            editor.apply()
            
            android.widget.Toast.makeText(this, "Settings saved", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
