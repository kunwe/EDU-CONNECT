package com.example.educonnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educonnect.data.local.entities.Question
import com.example.educonnect.data.local.entities.Quiz
import com.example.educonnect.data.local.entities.QuizAttempt
import com.example.educonnect.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    val quizzes: StateFlow<List<Quiz>> = repository.getQuizzesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attempts: StateFlow<List<QuizAttempt>> = repository.getAttemptsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentQuestions = MutableStateFlow<List<Question>>(emptyList())
    val currentQuestions: StateFlow<List<Question>> = _currentQuestions

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished

    init {
        viewModelScope.launch {
            repository.seedInitialQuizzes()
        }
    }

    fun startQuiz(quizId: String) {
        viewModelScope.launch {
            val allQuestions = repository.getQuestionsForQuiz(quizId)
            // Randomly select 5 questions out of the 15 available every time the user enters the quiz
            _currentQuestions.value = allQuestions.shuffled().take(5)
            _currentQuestionIndex.value = 0
            _score.value = 0
            _isQuizFinished.value = false
        }
    }

    fun submitAnswer(answer: String, onFeedback: (Boolean, String) -> Unit) {
        val questions = _currentQuestions.value
        val index = _currentQuestionIndex.value
        if (index >= questions.size) return

        val question = questions[index]
        val isCorrect = question.correctAnswer.trim().lowercase() == answer.trim().lowercase()

        if (isCorrect) {
            _score.value += 1
        }

        onFeedback(isCorrect, question.explanation)

        viewModelScope.launch {
            kotlinx.coroutines.delay(2000) // Delay to let user see feedback
            if (index + 1 < questions.size) {
                _currentQuestionIndex.value += 1
            } else {
                _isQuizFinished.value = true
                repository.saveAttempt(
                    QuizAttempt(
                        quizId = question.quizId,
                        score = ((_score.value.toFloat() / questions.size) * 100).toInt(),
                        timeTakenSeconds = 45
                    )
                )
            }
        }
    }
}
