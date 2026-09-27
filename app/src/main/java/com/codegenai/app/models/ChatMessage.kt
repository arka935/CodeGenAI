package com.codegenai.app.models

data class ChatMessage(
    val id: String = System.nanoTime().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long,
    val isCode: Boolean = false,
    val codeLanguage: String = "kotlin"
)
