package com.example.educonnect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable")
data class TimetableEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: String,
    val subjectTitle: String,
    val dayOfWeek: String, // e.g. "Monday"
    val timeSlot: String, // e.g. "09:00 - 10:30"
    val room: String = "Room 101"
)
