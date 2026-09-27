package com.codegenai.app.utils

import android.content.Context

class PreferencesManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    fun getApiKey(provider: String): String {
        return sharedPreferences.getString(provider, "") ?: ""
    }

    fun setApiKey(provider: String, key: String) {
        sharedPreferences.edit().putString(provider, key).apply()
    }

    fun getAllApiKeys(): Map<String, String> {
        return mapOf(
            "openai_key" to getApiKey("openai_key"),
            "gemini_key" to getApiKey("gemini_key"),
            "grok_key" to getApiKey("grok_key"),
            "groq_key" to getApiKey("groq_key"),
            "qwen_key" to getApiKey("qwen_key"),
            "deepseek_key" to getApiKey("deepseek_key"),
            "copilot_key" to getApiKey("copilot_key")
        )
    }
}
