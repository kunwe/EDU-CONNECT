package com.example.educonnect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: String,
    val title: String,
    val dueDate: String, // e.g. "Due: Tomorrow", "Due: 3 May"
    val statusColor: String = "Green", // Green, Orange, Gray
    val isCompleted: Boolean = false,
    val attachmentName: String = "",
    val attachmentType: String = "" // txt, png, pdf, word
)
