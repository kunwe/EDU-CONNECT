package com.example.educonnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.educonnect.databinding.FragmentSubjectsBinding
import com.google.android.material.checkbox.MaterialCheckBox
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    private val officialLanguages = listOf(
        "English", "Afrikaans", "isiZulu", "isiXhosa", "isiNdebele",
        "Sepedi", "Sesotho", "Setswana", "Siswati", "Tshivenda", "Xitsonga"
    )

    private val allElectives = listOf(
        "Physical Sciences", "Life Sciences", "Information Technology", "Engineering Graphics and Design",
        "Agricultural Sciences", "Agricultural Technology", "Agricultural Management Practices", "Technical Mathematics",
        "Technical Sciences", "Civil Technology", "Electrical Technology", "Mechanical Technology",
        "Maritime Economics", "Advanced Programme Mathematics", "Accounting", "Business Studies", "Economics",
        "Computer Applications Technology (CAT)", "Geography", "History", "Religion Studies", "Consumer Studies",
        "Hospitality Studies", "Tourism", "Dramatic Arts", "Visual Arts", "Design", "Music", "Dance Studies",
        "Sport and Exercise Science", "Equine Studies"
    )

    private val electiveCheckBoxes = mutableListOf<MaterialCheckBox>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdowns()
        setupElectiveCheckboxes()
        observeViewModel()

        binding.btnSaveSubjects.setOnClickListener {
            saveChanges()
        }
    }

    private fun setupDropdowns() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, officialLanguages)
        binding.actvHomeLanguage.setAdapter(adapter)
        binding.actvFirstAdditionalLanguage.setAdapter(adapter)
    }

    private fun setupElectiveCheckboxes() {
        binding.containerElectives.removeAllViews()
        allElectives.forEach { elective ->
            val checkBox = MaterialCheckBox(requireContext()).apply {
                text = elective
                id = View.generateViewId()
            }
            binding.containerElectives.addView(checkBox)
            electiveCheckBoxes.add(checkBox)
        }
    }

    private fun observeViewModel() {
        authViewModel.userProfileState.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                val user = resource.data
                populateFields(user)
            }
        }
    }

    private fun populateFields(user: User) {
        binding.actvHomeLanguage.setText(user.homeLanguage, false)
        binding.actvFirstAdditionalLanguage.setText(user.firstAdditionalLanguage, false)
        
        if (user.mathematics == "Mathematics") {
            binding.rbMaths.isChecked = true
        } else {
            binding.rbMathLit.isChecked = true
        }

        electiveCheckBoxes.forEach { cb ->
            cb.isChecked = user.electives.contains(cb.text.toString())
        }
    }

    private fun saveChanges() {
        val hl = binding.actvHomeLanguage.text.toString()
        val fal = binding.actvFirstAdditionalLanguage.text.toString()
        
        if (hl == fal) {
            Toast.makeText(requireContext(), "HL and FAL cannot be the same", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedElectives = electiveCheckBoxes.filter { it.isChecked }.map { it.text.toString() }
        if (selectedElectives.size != 3) {
            Toast.makeText(requireContext(), "Please select exactly 3 electives", Toast.LENGTH_SHORT).show()
            return
        }

        val currentProfile = (authViewModel.userProfileState.value as? Resource.Success)?.data ?: return
        
        val updatedUser = currentProfile.copy(
            homeLanguage = hl,
            firstAdditionalLanguage = fal,
            mathematics = if (binding.rbMaths.isChecked) "Mathematics" else "Mathematical Literacy",
            electives = selectedElectives
        )

        authViewModel.updateProfile(updatedUser)
        Toast.makeText(requireContext(), "Subjects updated successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}