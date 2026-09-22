package com.example.educonnect

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class ChatMessage(
    @DocumentId val messageId: String = "",
    val userId: String = "",
    val role: String = "user", // "user" or "model"
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Serializable
