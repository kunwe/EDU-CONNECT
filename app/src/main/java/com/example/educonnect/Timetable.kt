package com.example.educonnect

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class Timetable(
    @DocumentId val timetableId: String = "",
    val userId: String = "",
    val title: String = "",
    val entries: List<TimetableEntry> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

data class TimetableEntry(
    val day: String = "",
    val timeSlot: String = "",
    val subject: String = "",
    val activity: String = "",
    val duration: String = ""
) : Serializable
