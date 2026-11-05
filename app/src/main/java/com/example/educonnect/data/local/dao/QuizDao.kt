package com.example.educonnect.data.local.dao

import androidx.room.*
import com.example.educonnect.data.local.entities.Quiz
import com.example.educonnect.data.local.entities.Question
import com.example.educonnect.data.local.entities.QuizAttempt
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT * FROM quizzes")
    fun getAllQuizzesFlow(): Flow<List<Quiz>>

    @Query("SELECT * FROM questions WHERE quizId = :quizId")
    suspend fun getQuestionsForQuiz(quizId: String): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(quizzes: List<Quiz>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttempt)

    @Query("SELECT * FROM quiz_attempts")
    fun getAllAttemptsFlow(): Flow<List<QuizAttempt>>
}
