package com.example.educonnect

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.educonnect.databinding.ActivityTimetableBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimetableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimetableBinding
    private val viewModel: TimetableViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.sliderHours.addOnChangeListener { _, value, _ ->
            binding.tvHoursLabel.text = "Study Hours Per Day: ${value.toInt()}"
        }

        binding.btnGenerateTimetable.setOnClickListener {
            val selectedSubjects = mutableListOf<String>()
            for (i in 0 until binding.chipGroupSubjects.childCount) {
                val chip = binding.chipGroupSubjects.getChildAt(i) as Chip
                if (chip.isChecked) {
                    selectedSubjects.add(chip.text.toString())
                }
            }

            if (selectedSubjects.isEmpty()) {
                Toast.makeText(this, "Please select at least one subject", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // In a real app, we'd get the user's actual grade from their profile
            viewModel.generateTimetable(selectedSubjects, binding.sliderHours.value.toInt(), "12")
        }

        binding.rvTimetable.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnGenerateTimetable.isEnabled = !loading
        }

        viewModel.timetable.observe(this) { timetable ->
            timetable?.let {
                // For simplicity, using a basic adapter to show entries
                // A better implementation would group by day
                val adapter = TimetableAdapter(it.entries)
                binding.rvTimetable.adapter = adapter
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }
}
