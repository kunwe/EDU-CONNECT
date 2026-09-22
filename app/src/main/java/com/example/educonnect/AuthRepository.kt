package com.example.educonnect

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun registerUser(email: String, password: String, userProfile: User): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("User ID is null")
            val updatedProfile = userProfile.copy(uid = uid)
            firestore.collection("users").document(uid).set(updatedProfile).await()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred during registration")
        }
    }

    suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred during login")
        }
    }

    suspend fun getUserProfile(uid: String): Resource<User> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val userProfile = document.toObject(User::class.java)
            if (userProfile != null) {
                Resource.Success(userProfile)
            } else {
                Resource.Error("User profile not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred fetching profile")
        }
    }

    suspend fun updateUserProfile(user: User) = withContext(Dispatchers.IO) {
        val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in.")
        firestore.collection("users").document(uid).set(user).await()
    }

    suspend fun updateUserTerm(term: Int) = withContext(Dispatchers.IO) {
        val uid = firebaseAuth.currentUser?.uid ?: return@withContext
        firestore.collection("users").document(uid).update("currentTerm", term).await()
    }
}