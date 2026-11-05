package com.example.educonnect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grade_records")
data class GradeRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: String,
    val title: String,
    val scorePercentage: Int
)
