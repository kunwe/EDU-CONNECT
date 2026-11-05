package com.example.educonnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.educonnect.data.repository.AuthRepository
import com.example.educonnect.data.repository.ModuleRepository
import com.example.educonnect.data.repository.QuizRepository

class ViewModelFactory(
    private val authRepository: AuthRepository? = null,
    private val moduleRepository: ModuleRepository? = null,
    private val quizRepository: QuizRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository!!) as T
            }
            modelClass.isAssignableFrom(QuizViewModel::class.java) -> {
                QuizViewModel(quizRepository!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
