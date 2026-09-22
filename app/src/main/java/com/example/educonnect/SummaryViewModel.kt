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
class SummaryViewModel @Inject constructor(
    private val repository: SummaryRepository
) : ViewModel() {

    private val TAG = "SummaryViewModel"

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _summary = MutableLiveData<Summary?>()
    val summary: LiveData<Summary?> = _summary

    private val _summaryHistory = MutableLiveData<List<Summary>>()
    val summaryHistory: LiveData<List<Summary>> = _summaryHistory

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun generateSummary(rawText: String, subject: String, grade: String) {
        Log.d(TAG, "generateSummary text called")
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = repository.summarizeText(rawText, subject, grade)
                _summary.value = result
            } catch (e: Exception) {
                Log.e(TAG, "Error generating text summary: ${e.localizedMessage}")
                _errorMessage.value = e.localizedMessage ?: "Failed to generate text summary"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateImageSummary(imageBase64: String, mimeType: String, subject: String, grade: String) {
        Log.d(TAG, "generateImageSummary called")
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = repository.summarizeImage(imageBase64, mimeType, subject, grade)
                _summary.value = result
            } catch (e: Exception) {
                Log.e(TAG, "Error generating image summary: ${e.localizedMessage}")
                _errorMessage.value = e.localizedMessage ?: "Failed to generate image summary"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadSummaryHistory() {
        Log.d(TAG, "loadSummaryHistory called")
        viewModelScope.launch {
            try {
                val history = repository.getSummaryHistory()
                _summaryHistory.value = history
            } catch (e: Exception) {
                Log.e(TAG, "Error loading history: ${e.localizedMessage}")
            }
        }
    }

    fun loadPastSummary(summary: Summary) {
        Log.d(TAG, "loadPastSummary called for summaryId: ${summary.summaryId}")
        _summary.value = summary
    }

    fun reset() {
        Log.d(TAG, "reset state invoked")
        _summary.value = null
        _errorMessage.value = null
    }
}
