package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.educonnect.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    private val officialLanguages = listOf(
        "English", "Afrikaans", "isiZulu", "isiXhosa", "isiNdebele",
        "Sepedi", "Sesotho", "Setswana", "Siswati", "Tshivenda", "Xitsonga"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDropdowns()
        setupRegisterButton()
        observeViewModel()
    }

    private fun setupDropdowns() {
        val adapterHL = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, officialLanguages)
        val adapterFAL = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, officialLanguages)
        
        binding.actvHomeLanguage.setAdapter(adapterHL)
        binding.actvFirstAdditionalLanguage.setAdapter(adapterFAL)
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val surname = binding.etSurname.text.toString().trim()
            val grade = binding.etGrade.text.toString().trim()
            val subjectNumber = binding.etSubjectNumber.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val hl = binding.actvHomeLanguage.text.toString()
            val fal = binding.actvFirstAdditionalLanguage.text.toString()

            val mathsOption = if (binding.rbMaths.isChecked) "Mathematics" else "Mathematical Literacy"

            if (name.isEmpty() || surname.isEmpty() || grade.isEmpty() || subjectNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all personal details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (hl.isEmpty() || fal.isEmpty()) {
                Toast.makeText(this, "Please select both Home Language and First Additional Language", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (hl == fal) {
                Toast.makeText(this, "First Additional Language cannot be the same as Home Language", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val selectedElectives = getSelectedElectives()
            if (selectedElectives.size != 3) {
                Toast.makeText(this, "Please select exactly 3 elective subjects. Currently selected: ${selectedElectives.size}", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val userProfile = User(
                uid = "", // Will be populated by the AuthRepository with Firebase User Uid
                email = email,
                name = name,
                surname = surname,
                grade = grade,
                subjectNumber = subjectNumber,
                homeLanguage = hl,
                firstAdditionalLanguage = fal,
                mathematics = mathsOption,
                electives = selectedElectives,
                lifeOrientation = true
            )

            authViewModel.registerUser(email, password, userProfile)
        }
    }

    private fun getSelectedElectives(): List<String> {
        val electivesList = mutableListOf<String>()

        val mathSciBoxes = listOf(
            binding.cbPhysicalSciences, binding.cbLifeSciences, binding.cbIT, binding.cbEGD,
            binding.cbAgriSciences, binding.cbAgriTech, binding.cbAgriManagement, binding.cbTechMaths,
            binding.cbTechSciences, binding.cbCivilTech, binding.cbElectTech, binding.cbMechTech,
            binding.cbMaritime, binding.cbAPMaths
        )

        val commercialBoxes = listOf(
            binding.cbAccounting, binding.cbBusinessStudies, binding.cbEconomics, binding.cbCAT
        )

        val humanitiesBoxes = listOf(
            binding.cbGeography, binding.cbHistory, binding.cbReligion, binding.cbConsumer,
            binding.cbHospitality, binding.cbTourism, binding.cbDramaticArts, binding.cbVisualArts,
            binding.cbDesign, binding.cbMusic, binding.cbDance, binding.cbSportScience, binding.cbEquine
        )

        val allCheckboxes = mathSciBoxes + commercialBoxes + humanitiesBoxes

        for (checkBox in allCheckboxes) {
            if (checkBox.isChecked) {
                electivesList.add(checkBox.text.toString())
            }
        }

        return electivesList
    }

    private fun observeViewModel() {
        authViewModel.registerState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    binding.btnRegister.isEnabled = true
                    val uid = resource.data.user?.uid ?: ""
                    navigateToHome(uid)
                }
                is Resource.Error -> {
                    binding.btnRegister.isEnabled = true
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