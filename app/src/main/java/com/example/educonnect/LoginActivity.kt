package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.educonnect.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged in
        FirebaseAuth.getInstance().currentUser?.let { user ->
            navigateToHome(user.uid)
            return
        }

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, "Please fill in all fields", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            authViewModel.loginUser(email, password)
        }

        binding.btnGoogleLogin.setOnClickListener {
            Toast.makeText(this, "Google SSO Integration called", Toast.LENGTH_SHORT).show()
        }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun observeViewModel() {
        authViewModel.loginState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnLogin.isEnabled = false
                }
                is Resource.Success -> {
                    binding.btnLogin.isEnabled = true
                    val uid = resource.data.user?.uid ?: ""
                    navigateToHome(uid)
                }
                is Resource.Error -> {
                    binding.btnLogin.isEnabled = true
                    Snackbar.make(binding.root, resource.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToHome(uid: String) {
        // Changed target to MainContainerActivity for the new Bottom Navigation layout
        val intent = Intent(this, MainContainerActivity::class.java).apply {
            putExtra("USER_UID", uid)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}