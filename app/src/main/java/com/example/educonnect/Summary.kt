package com.example.educonnect

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class Summary(
    @DocumentId val summaryId: String = "",
    val userId: String = "",
    val title: String = "",
    val sourceType: String = "TEXT", // "TEXT" or "IMAGE"
    val originalText: String = "",
    val summaryText: String = "",
    val keyPoints: List<String> = emptyList(),
    val subject: String = "",
    val grade: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Serializable
