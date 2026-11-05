package com.example.educonnect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey val id: String,
    val quizId: String,
    val questionText: String,
    val questionType: String, // Multiple-Choice, True/False, Written Response
    val optionsString: String = "", // Comma separated options if Multiple-Choice
    val correctAnswer: String,
    val explanation: String = ""
)
