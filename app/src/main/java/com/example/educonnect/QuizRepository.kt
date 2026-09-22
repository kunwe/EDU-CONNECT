package com.example.educonnect

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val geminiApi: GeminiApi,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val TAG = "QuizRepo"
    private val gson = Gson()

    private fun getModelList(): List<String> = listOf(
        "gemini-3.5-flash",
        "gemini-3.5-flash-lite",
        "gemini-3.0-flash",
        "gemini-3.0-flash-lite",
        "gemini-2.5-flash",
        "gemini-2.5-flash-lite",
        "gemini-2.0-flash",
        "gemini-1.5-flash"
    )

    suspend fun generateAndSaveQuiz(topic: String, grade: String): Quiz = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid
            ?: throw Exception("User not logged in.")

        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            throw Exception("API Key is missing. Add GEMINI_API_KEY to local.properties.")
        }

        Log.d(TAG, "Generating quiz | Topic: $topic | Grade: $grade")
        val prompt = buildCapsPrompt(topic, grade)
        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            generationConfig = GenerationConfig(responseMimeType = "application/json")
        )

        var lastError = "Unknown error"

        for (model in getModelList()) {
            try {
                Log.d(TAG, "--> Trying model: $model")
                val response = geminiApi.generateContent(
                    model = model,
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = request
                )

                val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (jsonText.isNullOrBlank()) {
                    lastError = "Empty response from $model"
                    continue
                }

                val cleanedJson = jsonText.replace("```json", "").replace("```", "").trim()
                val parsedQuiz = gson.fromJson(cleanedJson, Quiz::class.java)

                if (parsedQuiz.questions.isEmpty()) {
                    lastError = "Empty quiz from $model"
                    continue
                }

                val quizToSave = parsedQuiz.copy(
                    userId = uid,
                    topic = topic,
                    grade = grade,
                    totalQuestions = parsedQuiz.questions.size,
                    createdAt = System.currentTimeMillis()
                )

                val docRef = firestore.collection("users")
                    .document(uid)
                    .collection("quizzes")
                    .document()

                val finalQuiz = quizToSave.copy(quizId = docRef.id)
                docRef.set(finalQuiz).await()

                Log.d(TAG, "SUCCESS: Saved quiz ${docRef.id} using $model")
                return@withContext finalQuiz

            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "JSON parse error from $model: ${e.message}")
                lastError = "JSON error: ${e.message}"
            } catch (e: Exception) {
                Log.e(TAG, "Model $model failed: ${e.message}")
                lastError = "API error: ${e.localizedMessage}"
            }
        }

        throw Exception("All models failed. Last error: $lastError")
    }

    suspend fun getQuizHistory(): List<Quiz> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext emptyList()
        try {
            val snapshot = firestore.collection("users")
                .document(uid)
                .collection("quizzes")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.toObjects(Quiz::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load history: ${e.message}")
            emptyList()
        }
    }

    suspend fun saveQuizResult(quizId: String, score: Int, total: Int) = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext
        try {
            firestore.collection("users")
                .document(uid)
                .collection("quizzes")
                .document(quizId)
                .update(
                    mapOf(
                        "score" to score,
                        "totalQuestions" to total,
                        "completed" to true
                    )
                ).await()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save result: ${e.message}")
        }
    }

    private fun buildCapsPrompt(topic: String, grade: String): String {
        return """
            You are an educational assistant for the South African CAPS (Curriculum and Assessment Policy Statement) curriculum.
            Generate a 5-question multiple-choice quiz on the topic "$topic" for a Grade $grade learner.
            Strict requirements:
            - Questions MUST align with the CAPS curriculum for Grade $grade.
            - Use South African context and examples where appropriate.
            - Each question must have exactly 4 options.
            - The "correctAnswerIndex" must be the 0-based index of the correct option.
            - The "explanation" must clearly justify why the answer is correct.
            Return ONLY a valid JSON object matching this exact structure:
            {
              "title": "Quiz Title Here",
              "questions": [
                {
                  "questionText": "Question text?",
                  "options": ["Option A", "Option B", "Option C", "Option D"],
                  "correctAnswerIndex": 0,
                  "explanation": "Why this is correct."
                }
              ]
            }
            Do not include markdown or conversational text. Output pure JSON only.
        """.trimIndent()
    }
}