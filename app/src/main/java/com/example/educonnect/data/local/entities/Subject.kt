package com.example.educonnect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey val id: String,
    val title: String,
    val teacherName: String,
    val progress: Int = 0, // 0 - 100
    val category: String = "General",
    val description: String = ""
)
