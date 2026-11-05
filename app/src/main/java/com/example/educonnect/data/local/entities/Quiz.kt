package com.example.educonnect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey val id: String,
    val subjectId: String,
    val title: String,
    val difficulty: String // Easy, Medium, Hard
)
