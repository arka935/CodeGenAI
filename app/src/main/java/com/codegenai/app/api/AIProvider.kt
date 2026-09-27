package com.codegenai.app.api

import android.content.Context
import com.codegenai.app.utils.PreferencesManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AIProvider(
    private val model: String,
    private val context: Context
) {
    private val httpClient = OkHttpClient()
    private val preferencesManager = PreferencesManager(context)

    suspend fun generateCode(prompt: String): String = withContext(Dispatchers.IO) {
        return@withContext when (model) {
            "openai" -> generateWithOpenAI(prompt)
            "gemini" -> generateWithGemini(prompt)
            "grok" -> generateWithGrok(prompt)
            "groq" -> generateWithGroq(prompt)
            "qwen" -> generateWithQwen(prompt)
            "deepseek" -> generateWithDeepSeek(prompt)
            "copilot" -> generateWithCopilot(prompt)
            else -> "Model not supported"
        }
    }

    private suspend fun generateWithOpenAI(prompt: String): String {
        val apiKey = preferencesManager.getApiKey("openai_key")
        if (apiKey.isEmpty()) return "Please set OpenAI API key in settings"

        val requestBody = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
            put("max_tokens", 2000)
        }.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        return try {
            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                val jsonResponse = JSONObject(responseBody)
                jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
            } else {
                "Error: ${response.code}"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private suspend fun generateWithGemini(prompt: String): String {
        val apiKey = preferencesManager.getApiKey("gemini_key")
        if (apiKey.isEmpty()) return "Please set Gemini API key in settings"

        val requestBody = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
        }.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$apiKey")
            .post(requestBody)
            .build()

        return try {
            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                val jsonResponse = JSONObject(responseBody)
                jsonResponse.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
            } else {
                "Error: ${response.code}"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private suspend fun generateWithGrok(prompt: String): String {
        val apiKey = preferencesManager.getApiKey("grok_key")
        if (apiKey.isEmpty()) return "Please set Grok API key in settings"
        return "Grok response for: $prompt"
    }

    private suspend fun generateWithGroq(prompt: String): String {
        val apiKey = preferencesManager.getApiKey("groq_key")
        if (apiKey.isEmpty()) return "Please set Groq API key in settings"
        return "Groq response for: $prompt"
    }

    private suspend fun generateWithQwen(prompt: String): String {
        val apiKey = preferencesManager.getApiKey("qwen_key")
        if (apiKey.isEmpty()) return "Please set Qwen API key in settings"
        return "Qwen response for: $prompt"
    }

    private suspend fun generateWithDeepSeek(prompt: String): String {
        val apiKey = preferencesManager.getApiKey("deepseek_key")
        if (apiKey.isEmpty()) return "Please set DeepSeek API key in settings"
        return "DeepSeek response for: $prompt"
    }

    private suspend fun generateWithCopilot(prompt: String): String {
        val apiKey = preferencesManager.getApiKey("copilot_key")
        if (apiKey.isEmpty()) return "Please set Copilot API key in settings"
        return "Copilot response for: $prompt"
    }
}
