package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.educonnect.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    private var userGrade: String = ""
    private var userSubjects: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSubjects.layoutManager = LinearLayoutManager(requireContext())

        setupButtons()
        observeViewModel()

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            authViewModel.getUserProfile(uid)
        }
    }

    private fun setupButtons() {
        binding.btnCareerAdvisor.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                putExtra("USER_GRADE", userGrade)
                putStringArrayListExtra("USER_SUBJECTS", userSubjects)
            }
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        authViewModel.userProfileState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.tvWelcome.text = "Welcome, Loading..."
                }
                is Resource.Success -> {
                    val userProfile = resource.data
                    userGrade = userProfile.grade
                    binding.tvWelcome.text = "Welcome, ${userProfile.name} ${userProfile.surname} - Grade ${userProfile.grade}"

                    val subjectList = mutableListOf<String>().apply {
                        add("Home Language: ${userProfile.homeLanguage}")
                        add("First Additional Language: ${userProfile.firstAdditionalLanguage}")
                        add(userProfile.mathematics)
                        if (userProfile.lifeOrientation) add("Life Orientation")
                        addAll(userProfile.electives)
                    }
                    userSubjects = ArrayList(subjectList)
                    binding.rvSubjects.adapter = SubjectAdapter(subjectList)
                    Log.d("HomeFragment", "Profile loaded for ${userProfile.email}")
                }
                is Resource.Error -> {
                    binding.tvWelcome.text = "Error loading profile"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}