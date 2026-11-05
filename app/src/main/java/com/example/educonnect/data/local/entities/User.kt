package com.example.educonnect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a User profile for the South African school context.
 * Compatible with Room (Local) and Firestore (Remote).
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey val uid: String = "",
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val grade: String = "", // e.g., "10", "11", "12"
    val subjectNumber: String = "",
    val homeLanguage: String = "",
    val firstAdditionalLanguage: String = "",
    val mathematics: String = "", // "Mathematics" or "Mathematical Literacy"
    val electives: List<String> = emptyList(), // Exactly 3 chosen subjects
    val lifeOrientation: Boolean = true, // Always true as it is compulsory
    val profilePhotoUrl: String = "",
    val points: Int = 0,
    val streak: Int = 0,
    val mastery: Int = 0,
    val legalName: String = "",
    val backupEmail: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val bio: String = "",
    val jobTitle: String = "",
    val profileVisibility: String = "Public",
    val searchDiscoverable: Boolean = true,
    val analyticsOptIn: Boolean = true,
    val adsOptIn: Boolean = false,
    val activityStatusEnabled: Boolean = true
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", "", "", "", "", "", "", "", emptyList(), true, "", 0, 0, 0, "", "", "", "", "", "", "Public", true, true, false, true)
}
