package com.example.educonnect

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.educonnect.databinding.ActivityQuizBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()

    // Holds the user's grade, passed from HomeActivity
    private var userGrade: String = "10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "AI Quiz"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Read the user's grade from the Intent (defaults to "10" if not found)
        userGrade = intent.getStringExtra("USER_GRADE") ?: "10"

        // Check if we're opening an existing quiz from history
        val existingQuiz = intent.getSerializableExtra("EXISTING_QUIZ") as? Quiz
        if (existingQuiz != null) {
            viewModel.loadPastQuiz(existingQuiz)
        }

        binding.btnGenerate.setOnClickListener {
            val topic = binding.etTopic.text.toString().trim()
            if (topic.isBlank()) {
                Toast.makeText(this, "Please enter a topic.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.generateQuiz(topic, userGrade)
        }

        binding.btnSubmit.setOnClickListener {
            val selectedId = binding.rgOptions.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedIndex = binding.rgOptions.indexOfChild(findViewById(selectedId))
            val (isCorrect, explanation) = viewModel.submitAnswer(selectedIndex)
            val message = if (isCorrect) "✅ Correct! $explanation" else "❌ Incorrect. $explanation"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            binding.rgOptions.clearCheck()
        }

        binding.btnRetake.setOnClickListener {
            binding.inputContainer.visibility = View.VISIBLE
            binding.quizContainer.visibility = View.GONE
            binding.resultContainer.visibility = View.GONE
        }

        observeViewModel()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnGenerate.isEnabled = !isLoading
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.quiz.observe(this) { quiz ->
            if (quiz != null && !viewModel.quizFinished.value!!) {
                binding.inputContainer.visibility = View.GONE
                binding.resultContainer.visibility = View.GONE
                binding.quizContainer.visibility = View.VISIBLE
                renderQuestion()
            }
        }

        viewModel.currentQuestionIndex.observe(this) {
            renderQuestion()
        }

        viewModel.score.observe(this) { score ->
            binding.tvScore.text = "Score: $score"
        }

        viewModel.quizFinished.observe(this) { finished ->
            if (finished) {
                binding.quizContainer.visibility = View.GONE
                binding.resultContainer.visibility = View.VISIBLE
                val total = viewModel.quiz.value?.questions?.size ?: 0
                binding.tvFinalScore.text = "You scored ${viewModel.score.value} / $total"
            }
        }
    }

    private fun renderQuestion() {
        val question = viewModel.getCurrentQuestion() ?: return
        val index = viewModel.currentQuestionIndex.value ?: 0
        val total = viewModel.quiz.value?.questions?.size ?: 0

        binding.tvQuestionNumber.text = "Question ${index + 1} of $total  •  Grade $userGrade"
        binding.tvQuestion.text = question.questionText

        binding.rgOptions.removeAllViews()
        question.options.forEach { option ->
            val rb = RadioButton(this).apply {
                id = View.generateViewId()
                text = option
                textSize = 16f
                setPadding(8, 16, 8, 16)
            }
            binding.rgOptions.addView(rb)
        }
    }
}