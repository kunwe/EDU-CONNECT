package com.example.educonnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educonnect.data.local.entities.User
import com.example.educonnect.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()
    val userState: StateFlow<User?> = _userProfile.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    fun register(email: String, password: String, user: User) {
        if (email.isBlank() || password.length < 6) {
            _uiState.value = AuthUiState.Error("Valid email and 6+ char password required.")
            return
        }
        if (user.homeLanguage == user.firstAdditionalLanguage) {
            _uiState.value = AuthUiState.Error("HL and FAL cannot be the same language.")
            return
        }
        if (user.electives.size != 3) {
            _uiState.value = AuthUiState.Error("You must select exactly 3 elective subjects.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val finalUser = repository.registerUser(email, password, user)
                _userProfile.value = finalUser
                _uiState.value = AuthUiState.Success(finalUser)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Registration failed.")
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Email and Password are required.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val profile = repository.loginUser(email, password)
                _userProfile.value = profile
                _uiState.value = AuthUiState.Success(profile)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Account not registered. Please register first.")
            }
        }
    }

    fun loginWithGoogle(email: String = "student@gmail.com", name: String = "Google Learner") {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val user = repository.loginWithGoogle(email, name)
                _userProfile.value = user
                _uiState.value = AuthUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Account not registered. Please register first.")
            }
        }
    }

    fun fetchCurrentUserProfile() {
        val currentUser = repository.getCurrentUser()
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    var profile = repository.getLocalUser(currentUser.uid)
                    if (profile == null) {
                        profile = repository.getUserProfile(currentUser.uid)
                    }
                    _userProfile.value = profile
                } catch (e: Exception) {
                    // Handle failure
                }
            }
        }
    }

    fun updateExpandedProfile(user: User) {
        _userProfile.value = user
    }

    fun updateProfile(name: String, photoUrl: String) {
        val current = _userProfile.value ?: return
        val updated = current.copy(name = name, profilePhotoUrl = photoUrl)
        _userProfile.value = updated
    }

    fun changePassword(oldPass: String, newPass: String, confirmPass: String, onSuccess: () -> Unit) {
        if (newPass != confirmPass) {
            _errorState.value = "New passwords do not match."
            return
        }
        if (newPass.length < 6) {
            _errorState.value = "Password must be at least 6 characters."
            return
        }
        _errorState.value = null
        onSuccess()
    }

    fun changePassword(oldPass: String, newPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (newPass.length >= 6) {
            onSuccess()
        } else {
            onError("Password must be at least 6 characters.")
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    fun logout() {
        repository.logout()
        _userProfile.value = null
        _uiState.value = AuthUiState.Idle
    }
}
