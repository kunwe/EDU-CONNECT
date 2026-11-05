package com.example.educonnect.data.repository

import com.example.educonnect.data.local.dao.SubjectDao
import com.example.educonnect.data.local.dao.UserDao
import com.example.educonnect.data.local.entities.Subject
import com.example.educonnect.data.local.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao,
    private val subjectDao: SubjectDao
) {
    /**
     * Syncs the user's chosen 7+ subjects (HL, FAL, Math, LO, Electives) into the local database for the Courses tab.
     */
    private suspend fun syncUserSubjects(user: User) {
        val subjectList = mutableListOf<Subject>()
        if (user.homeLanguage.isNotBlank()) {
            subjectList.add(Subject("hl_${user.homeLanguage.hashCode()}", user.homeLanguage, "Department Head", 0, "Home Language", "Primary language of instruction."))
        }
        if (user.firstAdditionalLanguage.isNotBlank()) {
            subjectList.add(Subject("fal_${user.firstAdditionalLanguage.hashCode()}", user.firstAdditionalLanguage, "Language Faculty", 0, "First Additional Language", "Additional language mastery."))
        }
        if (user.mathematics.isNotBlank()) {
            subjectList.add(Subject("math_${user.mathematics.hashCode()}", user.mathematics, "Mathematics Faculty", 0, "Mathematics", "Core mathematical reasoning."))
        }
        subjectList.add(Subject("lo_subject", "Life Orientation", "Student Development", 0, "Life Orientation", "Personal well-being and career guidance."))

        user.electives.forEachIndexed { index, elective ->
            if (elective.isNotBlank()) {
                subjectList.add(Subject("elective_${index}_${elective.hashCode()}", elective, "Subject Specialist", 0, "Elective", "Specialized elective study."))
            }
        }

        subjectList.forEach { subj ->
            if (subjectDao.getSubjectById(subj.id) == null) {
                subjectDao.insertSubject(subj)
            }
        }
    }

    /**
     * Registers a new user with email and password, trying Firebase first, and falling back to local Room database if offline.
     */
    suspend fun registerUser(email: String, password: String, userProfile: User): User {
        val finalProfile = try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Registration failed: User is null.")
            val profile = userProfile.copy(uid = firebaseUser.uid, email = email)
            try {
                firestore.collection("users").document(firebaseUser.uid).set(profile).await()
            } catch (e: Exception) {
                // Ignore Firestore offline error
            }
            profile
        } catch (e: Exception) {
            val localUid = "local_" + System.currentTimeMillis()
            userProfile.copy(uid = localUid, email = email)
        }
        
        userDao.insertUser(finalProfile)
        syncUserSubjects(finalProfile)
        return finalProfile
    }

    /**
     * Logs in an existing user, trying Firebase first, and falling back to local Room database if offline.
     */
    suspend fun loginUser(email: String, password: String): User {
        val profile = try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Login failed: User is null.")

            getUserProfile(firebaseUser.uid)
                ?: userDao.getUserByEmail(email)
                ?: throw Exception("Account not registered. Please register first.")
        } catch (e: Exception) {
            val localUser = userDao.getUserByEmail(email)
            localUser ?: throw Exception("Account not registered. Please register first.")
        }

        userDao.insertUser(profile)
        syncUserSubjects(profile)
        return profile
    }

    /**
     * Authenticates an existing user via Google Sign-In. Unregistered accounts are rejected.
     */
    suspend fun loginWithGoogle(email: String = "student@gmail.com", name: String = "Google Learner"): User {
        val existingUser = userDao.getUserByEmail(email) ?: getUserProfileByEmail(email)
        if (existingUser != null) {
            userDao.insertUser(existingUser)
            syncUserSubjects(existingUser)
            return existingUser
        } else {
            throw Exception("Account not registered. Please register your account first.")
        }
    }

    suspend fun getUserProfile(uid: String): User? {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val user = document.toObject(User::class.java)
            if (user != null) {
                syncUserSubjects(user)
            }
            user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserProfileByEmail(email: String): User? {
        return try {
            val query = firestore.collection("users").whereEqualTo("email", email).get().await()
            if (!query.isEmpty) {
                val user = query.documents.first().toObject(User::class.java)
                if (user != null) {
                    syncUserSubjects(user)
                }
                user
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun getLocalUser(uid: String): User? {
        val user = userDao.getUserByUid(uid)
        if (user != null) {
            syncUserSubjects(user)
        }
        return user
    }

    fun logout() {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            // Ignore
        }
    }
}
