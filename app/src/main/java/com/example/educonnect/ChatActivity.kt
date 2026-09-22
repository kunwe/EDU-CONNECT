package com.example.educonnect

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.educonnect.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private val TAG = "ChatActivity"
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    private var userGrade: String = "10"
    private var userSubjects: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userGrade = intent.getStringExtra("USER_GRADE") ?: "10"
        userSubjects = intent.getStringArrayListExtra("USER_SUBJECTS") ?: emptyList()

        Log.d(TAG, "ChatActivity started for Grade $userGrade with subjects: $userSubjects")

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        viewModel.loadHistory()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
        }
    }

    private fun setupListeners() {
        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(text, userGrade, userSubjects)
                binding.etMessage.setText("")
            }
        }
    }

    private fun observeViewModel() {
        viewModel.messages.observe(this) { messages ->
            if (messages.isEmpty()) {
                // Show welcome message if no history exists
                val welcomeMsg = ChatMessage(
                    role = "model",
                    content = "Hi! I'm your Career Advisor. Ask me about careers, university requirements, or bursaries. 🎓",
                    timestamp = System.currentTimeMillis()
                )
                chatAdapter.submitList(listOf(welcomeMsg))
            } else {
                chatAdapter.submitList(messages) {
                    // Scroll to bottom after list is updated
                    if (messages.isNotEmpty()) {
                        binding.rvChat.scrollToPosition(messages.size - 1)
                    }
                }
            }
        }

        viewModel.isSending.observe(this) { isSending ->
            binding.progressBar.visibility = if (isSending) View.VISIBLE else View.GONE
            binding.btnSend.isEnabled = !isSending
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
