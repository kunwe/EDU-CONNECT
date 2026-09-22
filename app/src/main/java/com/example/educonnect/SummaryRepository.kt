package com.example.educonnect

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SummaryRepository @Inject constructor(
    private val geminiApi: GeminiApi,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val TAG = "SummaryRepo"
    private val gson = Gson()
    
    private val modelFallback = listOf(
        "gemini-3.5-flash",
        "gemini-3.5-flash-lite",
        "gemini-3.0-flash",
        "gemini-3.0-flash-lite",
        "gemini-2.5-flash",
        "gemini-2.0-flash",
        "gemini-1.5-flash"
    )

    suspend fun summarizeText(rawText: String, subject: String, grade: String): Summary = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in.")
        Log.d(TAG, "summarizeText called | Subject: $subject, Grade: $grade")
        
        val truncatedText = if (rawText.length > 30000) rawText.substring(0, 30000) else rawText
        val prompt = buildSummaryPrompt(subject, grade, truncatedText)

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            generationConfig = GenerationConfig(responseMimeType = "application/json")
        )

        val parsedResult = executeWithFallback(request)
        val finalSummary = parsedResult.copy(
            userId = uid,
            sourceType = "TEXT",
            originalText = if (truncatedText.length > 500) truncatedText.substring(0, 500) else truncatedText,
            subject = subject,
            grade = grade,
            createdAt = System.currentTimeMillis()
        )

        saveToFirestore(uid, finalSummary)
    }

    suspend fun summarizeImage(imageBase64: String, mimeType: String, subject: String, grade: String): Summary = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in.")
        Log.d(TAG, "summarizeImage called | Subject: $subject, Grade: $grade, MimeType: $mimeType")

        val prompt = "Extract and summarize this textbook page. Ensure it aligns strictly with the South African CAPS curriculum for Grade $grade $subject. Return a valid JSON object matching the requested structure."
        val promptPart = Part(text = prompt)
        val imagePart = Part(inlineData = InlineData(mimeType = mimeType, data = imageBase64))

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(promptPart, imagePart))),
            generationConfig = GenerationConfig(responseMimeType = "application/json")
        )

        val parsedResult = executeWithFallback(request)
        val finalSummary = parsedResult.copy(
            userId = uid,
            sourceType = "IMAGE",
            originalText = "[Image Uploaded]",
            subject = subject,
            grade = grade,
            createdAt = System.currentTimeMillis()
        )

        saveToFirestore(uid, finalSummary)
    }

    private suspend fun executeWithFallback(request: GeminiRequest): Summary {
        var lastError: Exception? = null

        for (model in modelFallback) {
            try {
                Log.d(TAG, "Attempting API call using model: $model")
                val response = geminiApi.generateContent(model, BuildConfig.GEMINI_API_KEY, request)
                val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                
                if (jsonText.isNullOrBlank()) {
                    Log.e(TAG, "Empty response string returned from model: $model")
                    continue
                }

                val cleanedJson = jsonText.replace("```json", "").replace("```", "").trim()
                val summaryObj = gson.fromJson(cleanedJson, Summary::class.java)
                
                if (summaryObj.summaryText.isBlank()) {
                    Log.e(TAG, "Parsed summaryText is empty for model: $model")
                    continue
                }

                Log.d(TAG, "Successfully generated content using model: $model")
                return summaryObj
            } catch (e: Exception) {
                Log.e(TAG, "Error executing model $model: ${e.localizedMessage}")
                lastError = e
            }
        }
        throw lastError ?: Exception("All models failed to return a valid summary response.")
    }

    private suspend fun saveToFirestore(uid: String, summary: Summary): Summary {
        val docRef = firestore.collection("users")
            .document(uid)
            .collection("summaries")
            .document()
            
        val summaryWithId = summary.copy(summaryId = docRef.id)
        docRef.set(summaryWithId).await()
        Log.d(TAG, "Saved summary successfully to Firestore with id: ${docRef.id}")
        return summaryWithId
    }

    suspend fun getSummaryHistory(): List<Summary> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext emptyList()
        Log.d(TAG, "getSummaryHistory called for uid: $uid")
        try {
            val snapshot = firestore.collection("users")
                .document(uid)
                .collection("summaries")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.toObjects(Summary::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading history from Firestore: ${e.localizedMessage}")
            emptyList()
        }
    }

    private fun buildSummaryPrompt(subject: String, grade: String, text: String): String {
        return """
            You are an educational summarizer expert for the South African CAPS (Curriculum and Assessment Policy Statement) guidelines.
            Analyze and summarize the following Grade $grade $subject textbook textbook snippet.
            Provide a strict JSON formatted response matching this exact structure:
            {
              "title": "Clear Topic Title",
              "summaryText": "A comprehensive detailed paragraph summarizing the material.",
              "keyPoints": [
                "Essential key point or concept definition 1",
                "Essential key point or concept definition 2"
              ]
            }
            Do not wrap the text in markdown format blocks or prefix conversation. Output pure JSON only.
            Textbook content to process:
            $text
        """.trimIndent()
    }
}
