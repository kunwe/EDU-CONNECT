package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.educonnect.databinding.ActivityQuizHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizHistoryBinding
    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My Quiz History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = QuizHistoryAdapter { quiz ->
            val intent = Intent(this, QuizActivity::class.java).apply {
                putExtra("EXISTING_QUIZ", quiz)
                putExtra("USER_GRADE", quiz.grade) // Pass the original grade
            }
            startActivity(intent)
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter

        viewModel.quizHistory.observe(this) { history ->
            binding.tvEmpty.visibility = if (history.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(history)
        }

        viewModel.loadQuizHistory()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}