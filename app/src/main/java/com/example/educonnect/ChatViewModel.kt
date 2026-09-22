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
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val TAG = "ChatViewModel"

    private val _isSending = MutableLiveData<Boolean>(false)
    val isSending: LiveData<Boolean> = _isSending

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadHistory() {
        Log.d(TAG, "loadHistory called")
        viewModelScope.launch {
            try {
                val history = repository.getChatHistory()
                _messages.value = history
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load chat history: ${e.localizedMessage}")
                _errorMessage.value = e.localizedMessage ?: "Failed to load chat history"
            }
        }
    }

    fun sendMessage(userText: String, grade: String, subjects: List<String>) {
        if (userText.isBlank()) return
        Log.d(TAG, "sendMessage called with content length: ${userText.length}")

        val currentList = _messages.value.orEmpty().toMutableList()
        val tempUserMsg = ChatMessage(
            messageId = "temp_${System.currentTimeMillis()}",
            userId = "",
            role = "user",
            content = userText,
            timestamp = System.currentTimeMillis()
        )
        
        // Optimistically append the user message
        currentList.add(tempUserMsg)
        _messages.value = currentList

        viewModelScope.launch {
            _isSending.value = true
            _errorMessage.value = null
            try {
                // Pass current history excluding the optimistically added message
                val historyToPass = currentList.dropLast(1)
                val resultPair = repository.sendMessage(userText, historyToPass, grade, subjects)
                
                // Replace matching history with accurate persistent response entries from Firestore
                val updatedList = historyToPass.toMutableList().apply {
                    add(resultPair.first)
                    add(resultPair.second)
                }
                _messages.value = updatedList
            } catch (e: Exception) {
                Log.e(TAG, "Error handling message transmission: ${e.localizedMessage}")
                _errorMessage.value = e.localizedMessage ?: "Failed to transmit message"
                
                // Remove optimistic message on error failure response
                val rollbackList = _messages.value.orEmpty().toMutableList()
                if (rollbackList.isNotEmpty()) {
                    rollbackList.remove(tempUserMsg)
                    _messages.value = rollbackList
                }
            } finally {
                _isSending.value = false
            }
        }
    }
}
