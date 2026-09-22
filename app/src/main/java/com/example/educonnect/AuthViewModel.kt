package com.example.educonnect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableLiveData<Resource<AuthResult>>()
    val registerState: LiveData<Resource<AuthResult>> = _registerState

    private val _loginState = MutableLiveData<Resource<AuthResult>>()
    val loginState: LiveData<Resource<AuthResult>> = _loginState

    private val _userProfileState = MutableLiveData<Resource<User>>()
    val userProfileState: LiveData<Resource<User>> = _userProfileState

    fun registerUser(email: String, password: String, userProfile: User) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading
            _registerState.value = authRepository.registerUser(email, password, userProfile)
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = authRepository.loginUser(email, password)
        }
    }

    fun getUserProfile(uid: String) {
        viewModelScope.launch {
            _userProfileState.value = Resource.Loading
            _userProfileState.value = authRepository.getUserProfile(uid)
        }
    }

    fun updateProfile(user: User) {
        viewModelScope.launch {
            try {
                authRepository.updateUserProfile(user)
                getUserProfile(user.uid)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateTerm(term: Int) {
        viewModelScope.launch {
            try {
                authRepository.updateUserTerm(term)
                val current = _userProfileState.value
                if (current is Resource.Success) {
                    _userProfileState.value = Resource.Success(current.data.copy(currentTerm = term))
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}