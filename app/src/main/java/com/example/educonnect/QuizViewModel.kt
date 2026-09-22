package com.example.educonnect

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val TAG = "QuizViewModel"

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _quiz = MutableLiveData<Quiz?>()
    val quiz: LiveData<Quiz?> = _quiz

    private val _quizHistory = MutableLiveData<List<Quiz>>(emptyList())
    val quizHistory: LiveData<List<Quiz>> = _quizHistory

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _quizFinished = MutableLiveData(false)
    val quizFinished: LiveData<Boolean> = _quizFinished

    fun generateQuiz(topic: String, grade: String) {
        if (topic.isBlank()) {
            _errorMessage.value = "Please enter a topic."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _quiz.value = null
            _currentQuestionIndex.value = 0
            _score.value = 0
            _quizFinished.value = false

            try {
                val generated = quizRepository.generateAndSaveQuiz(topic, grade)
                _quiz.value = generated
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                _errorMessage.value = e.message ?: "Failed to generate quiz."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitAnswer(selectedIndex: Int): Pair<Boolean, String> {
        val currentQuiz = _quiz.value ?: return Pair(false, "")
        val currentIndex = _currentQuestionIndex.value ?: 0
        val question = currentQuiz.questions.getOrNull(currentIndex) ?: return Pair(false, "")

        val isCorrect = selectedIndex == question.correctAnswerIndex
        if (isCorrect) {
            _score.value = (_score.value ?: 0) + 1
        }

        val explanation = question.explanation

        if (currentIndex + 1 < currentQuiz.questions.size) {
            _currentQuestionIndex.value = currentIndex + 1
        } else {
            _quizFinished.value = true
            viewModelScope.launch {
                quizRepository.saveQuizResult(
                    quizId = currentQuiz.quizId,
                    score = _score.value ?: 0,
                    total = currentQuiz.questions.size
                )
            }
        }

        return Pair(isCorrect, explanation)
    }

    fun loadQuizHistory() {
        viewModelScope.launch {
            _quizHistory.value = quizRepository.getQuizHistory()
        }
    }

    fun loadPastQuiz(quizToLoad: Quiz) {
        _quiz.value = quizToLoad
        _currentQuestionIndex.value = 0
        _score.value = quizToLoad.score
        _quizFinished.value = true
    }

    fun getCurrentQuestion(): QuizQuestion? {
        val currentQuiz = _quiz.value ?: return null
        val currentIndex = _currentQuestionIndex.value ?: 0
        return currentQuiz.questions.getOrNull(currentIndex)
    }
}