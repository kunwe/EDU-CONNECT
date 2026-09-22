package com.example.educonnect

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val grade: String = "",
    val subjectNumber: String = "",
    val homeLanguage: String = "",
    val firstAdditionalLanguage: String = "",
    val mathematics: String = "",
    val electives: List<String> = emptyList(),
    val lifeOrientation: Boolean = true,
    val currentTerm: Int = 1
)