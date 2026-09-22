package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.educonnect.databinding.FragmentQuizzesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizzesFragment : Fragment() {

    private var _binding: FragmentQuizzesBinding? = null
    private val binding get() = _binding!!

    private var userGrade: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizzesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get grade from arguments if passed, otherwise default to 10
        userGrade = arguments?.getString("USER_GRADE") ?: "10"

        binding.btnStartQuiz.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java).apply {
                putExtra("USER_GRADE", userGrade)
            }
            startActivity(intent)
        }

        binding.btnQuizHistory.setOnClickListener {
            val intent = Intent(requireContext(), QuizHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}