package com.example.educonnect

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val geminiApi: GeminiApi,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val tag = "ChatRepo"

    private val modelFallback = listOf(
        "gemini-3.5-flash",
        "gemini-3.5-flash-lite",
        "gemini-3.0-flash",
        "gemini-3.0-flash-lite",
        "gemini-2.5-flash",
        "gemini-2.5-flash-lite",
        "gemini-2.0-flash",
        "gemini-1.5-flash"
    )

    suspend fun sendMessage(
        userMessage: String,
        chatHistory: List<ChatMessage>,
        grade: String,
        subjects: List<String>
    ): Pair<ChatMessage, ChatMessage> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in.")

        // System instruction (no role needed for system_instruction)
        val systemContent = Content(
            parts = listOf(
                Part(
                    text = """
                        You are EduConnect's Career Advisor for South African learners.
                        The learner is in Grade $grade and takes these subjects: ${subjects.joinToString(", ")}.
                        Advise on: careers matching their subjects, university requirements (APS, matric points)
                        for SA universities (UCT, Wits, UP, UKZN, Stellenbosch, UNISA, TUT, CPUT, DUT),
                        TVET colleges, learnerships, SETAs, and NSFAS bursaries.
                        Keep responses under 200 words. Be practical and encouraging.
                    """.trimIndent()
                )
            )
        )

        // Build chat contents from history (role = "user" or "model")
        val contents = chatHistory.map { msg ->
            Content(
                role = msg.role,
                parts = listOf(Part(text = msg.content))
            )
        }.toMutableList()

        // Append the new user message
        contents.add(
            Content(
                role = "user",
                parts = listOf(Part(text = userMessage))
            )
        )

        val request = GeminiRequest(
            contents = contents,
            generationConfig = null,      // Chat is plain text, no JSON needed
            systemInstruction = systemContent
        )

        var aiResponseText = ""
        var lastError = "Unknown error"

        for (model in modelFallback) {
            try {
                Log.d(tag, "--> Trying model: $model")
                val response = geminiApi.generateContent(model, BuildConfig.GEMINI_API_KEY, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!text.isNullOrBlank()) {
                    aiResponseText = text
                    Log.d(tag, "SUCCESS with $model")
                    break
                } else {
                    lastError = "Empty response from $model"
                }
            } catch (e: Exception) {
                Log.e(tag, "Model $model failed: ${e.message}")
                lastError = "API error: ${e.localizedMessage}"
            }
        }

        if (aiResponseText.isBlank()) {
            throw Exception("AI failed to respond. Last error: $lastError")
        }

        val userMsg = ChatMessage(userId = uid, role = "user", content = userMessage)
        val aiMsg = ChatMessage(userId = uid, role = "model", content = aiResponseText)

        // Save both messages to Firestore in a batch
        val batch = firestore.batch()
        val chatCol = firestore.collection("users").document(uid).collection("chats")

        val uDoc = chatCol.document()
        val aDoc = chatCol.document()

        batch.set(uDoc, userMsg.copy(messageId = uDoc.id, timestamp = System.currentTimeMillis()))
        batch.set(aDoc, aiMsg.copy(messageId = aDoc.id, timestamp = System.currentTimeMillis() + 10))
        batch.commit().await()

        Pair(userMsg, aiMsg)
    }

    suspend fun getChatHistory(): List<ChatMessage> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext emptyList()
        try {
            val snapshot = firestore.collection("users").document(uid).collection("chats")
                .orderBy("timestamp", Query.Direction.ASCENDING).get().await()
            snapshot.toObjects(ChatMessage::class.java)
        } catch (e: Exception) {
            Log.e(tag, "Failed to load chat history: ${e.message}")
            emptyList()
        }
    }
}