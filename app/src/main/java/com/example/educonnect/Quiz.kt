package com.example.educonnect

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class Quiz(
    @DocumentId val quizId: String = "",
    val userId: String = "",
    val title: String = "",
    val topic: String = "",
    val grade: String = "",
    val questions: List<QuizQuestion> = emptyList(),
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

data class QuizQuestion(
    val questionText: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0,
    val explanation: String = ""
) : Serializable