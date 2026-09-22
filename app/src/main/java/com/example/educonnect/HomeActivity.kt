package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.educonnect.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val authViewModel: AuthViewModel by viewModels()

    private var userGrade: String = ""
    private var userSubjects: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.rvSubjects.layoutManager = LinearLayoutManager(this)

        val uid = intent.getStringExtra("USER_UID") ?: FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            authViewModel.getUserProfile(uid)
            observeViewModel()
        } else {
            Toast.makeText(this, "User profile session error", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }

        setupButtons()
    }

    private fun setupButtons() {
        binding.btnStartAiQuiz.setOnClickListener {
            if (userGrade.isBlank()) {
                Toast.makeText(this, "Profile still loading. Please wait.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, QuizActivity::class.java).apply {
                putExtra("USER_GRADE", userGrade)
            }
            startActivity(intent)
        }

        binding.btnCareerAdvisor.setOnClickListener {
            if (userGrade.isBlank()) {
                Toast.makeText(this, "Profile still loading...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("USER_GRADE", userGrade)
                putStringArrayListExtra("USER_SUBJECTS", userSubjects)
            }
            startActivity(intent)
        }

        binding.btnQuizHistory.setOnClickListener {
            startActivity(Intent(this, QuizHistoryActivity::class.java))
        }
    }

    private fun observeViewModel() {
        authViewModel.userProfileState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.tvWelcome.text = "Welcome, Loading..."
                }
                is Resource.Success -> {
                    val userProfile = resource.data
                    userGrade = userProfile.grade

                    binding.tvWelcome.text =
                        "Welcome, ${userProfile.name} ${userProfile.surname} - Grade ${userProfile.grade}"

                    val subjectList = mutableListOf<String>().apply {
                        add("Home Language: ${userProfile.homeLanguage}")
                        add("First Additional Language: ${userProfile.firstAdditionalLanguage}")
                        add(userProfile.mathematics)
                        if (userProfile.lifeOrientation) add("Life Orientation")
                        addAll(userProfile.electives)
                    }
                    
                    userSubjects = ArrayList(subjectList)
                    binding.rvSubjects.adapter = SubjectAdapter(subjectList)
                }
                is Resource.Error -> {
                    binding.tvWelcome.text = "Error loading profile info"
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}