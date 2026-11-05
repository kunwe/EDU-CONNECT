package com.example.educonnect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quizId: String,
    val score: Int,
    val timeTakenSeconds: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
