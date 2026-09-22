package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.educonnect.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTermDropdown()
        observeViewModel()
        setupListeners()
    }

    private fun setupTermDropdown() {
        val terms = listOf("Term 1", "Term 2", "Term 3", "Term 4")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, terms)
        binding.actvTerm.setAdapter(adapter)

        binding.actvTerm.setOnItemClickListener { _, _, position, _ ->
            val selectedTerm = position + 1
            authViewModel.updateTerm(selectedTerm)
            Toast.makeText(requireContext(), "Term updated to Term $selectedTerm", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        authViewModel.userProfileState.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                val user = resource.data
                binding.tvUserName.text = "${user.name} ${user.surname}"
                binding.tvUserEmail.text = user.email
                binding.tvUserGrade.text = "Grade ${user.grade}"
                
                val termText = "Term ${user.currentTerm}"
                binding.actvTerm.setText(termText, false)
            }
        }
    }

    private fun setupListeners() {
        binding.btnChangePassword.setOnClickListener {
            Toast.makeText(requireContext(), "Change password feature coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Log Out")
            .setMessage("Are you sure you want to log out?")
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}