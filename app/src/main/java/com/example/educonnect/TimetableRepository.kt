package com.example.educonnect

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimetableRepository @Inject constructor(
    private val geminiApi: GeminiApi,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
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

    suspend fun generateTimetable(subjects: List<String>, hoursPerDay: Int, grade: String): Timetable = withContext(Dispatchers.IO) {
        val prompt = """
            Generate a weekly study timetable for a Grade $grade student taking these subjects: ${subjects.joinToString()}.
            The student can study $hoursPerDay hours per day. 
            Balance the subjects across Monday to Sunday according to CAPS curriculum difficulty.
            Return a JSON object with this structure:
            {
              "title": "Grade $grade Study Timetable",
              "entries": [
                { "day": "Monday", "timeSlot": "16:00 - 17:00", "subject": "Subject Name", "activity": "Study/Revise", "duration": "1 hour" }
              ]
            }
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            generationConfig = GenerationConfig(responseMimeType = "application/json")
        )

        var lastError: Exception? = null
        for (model in modelFallback) {
            try {
                val response = geminiApi.generateContent(model, BuildConfig.GEMINI_API_KEY, request)
                val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: throw Exception("Empty response")
                val cleanedJson = jsonText.replace("```json", "").replace("```", "").trim()
                val timetable = gson.fromJson(cleanedJson, Timetable::class.java)
                
                return@withContext saveTimetable(timetable)
            } catch (e: Exception) {
                lastError = e
            }
        }
        throw lastError ?: Exception("Timetable generation failed")
    }

    private suspend fun saveTimetable(timetable: Timetable): Timetable {
        val uid = auth.currentUser?.uid ?: throw Exception("Not logged in")
        val docRef = firestore.collection("users").document(uid).collection("timetables").document()
        val finalTimetable = timetable.copy(userId = uid, timetableId = docRef.id)
        docRef.set(finalTimetable).await()
        return finalTimetable
    }

    suspend fun getTimetables(): List<Timetable> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext emptyList()
        firestore.collection("users").document(uid).collection("timetables")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get().await().toObjects(Timetable::class.java)
    }
}
