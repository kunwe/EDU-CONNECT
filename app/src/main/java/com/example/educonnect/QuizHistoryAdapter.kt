package com.example.educonnect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.databinding.ItemQuizHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizHistoryAdapter(
    private val onQuizClick: (Quiz) -> Unit
) : ListAdapter<Quiz, QuizHistoryAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Quiz>() {
            override fun areItemsTheSame(a: Quiz, b: Quiz) = a.quizId == b.quizId
            override fun areContentsTheSame(a: Quiz, b: Quiz) = a == b
        }
    }

    inner class VH(val binding: ItemQuizHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemQuizHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val quiz = getItem(position)
        holder.binding.apply {
            tvQuizTitle.text = quiz.title.ifBlank { quiz.topic }
            tvQuizMeta.text = "${quiz.grade} • ${quiz.questions.size} questions"

            val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(Date(quiz.createdAt))
            tvQuizDate.text = date

            if (quiz.completed) {
                tvQuizScore.text = "${quiz.score} / ${quiz.totalQuestions}"
                tvQuizScore.setTextColor(
                    root.context.getColor(android.R.color.holo_green_dark)
                )
            } else {
                tvQuizScore.text = "Not completed"
                tvQuizScore.setTextColor(
                    root.context.getColor(android.R.color.darker_gray)
                )
            }

            root.setOnClickListener { onQuizClick(quiz) }
        }
    }
}