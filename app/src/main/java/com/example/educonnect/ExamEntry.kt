package com.example.educonnect

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class ExamEntry(
    @DocumentId val examId: String = "",
    val userId: String = "",
    val subject: String = "",
    val examDate: Long = 0L,
    val examType: String = "", // "TEST" or "EXAM"
    val paperNumber: String = "",
    val duration: String = ""
) : Serializable
