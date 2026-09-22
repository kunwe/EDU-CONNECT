package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.educonnect.databinding.ActivitySummaryHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryHistoryActivity : AppCompatActivity() {

    private val TAG = "SummaryHistoryActivity"
    private lateinit var binding: ActivitySummaryHistoryBinding
    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate called")

        supportActionBar?.title = "Saved Summaries"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = SummaryHistoryAdapter { summary ->
            Log.d(TAG, "Summary clicked: ${summary.summaryId}")
            val intent = Intent(this, SummaryActivity::class.java).apply {
                putExtra("EXISTING_SUMMARY", summary)
                putExtra("USER_GRADE", summary.grade)
            }
            startActivity(intent)
        }

        binding.rvSummaries.layoutManager = LinearLayoutManager(this)
        binding.rvSummaries.adapter = adapter

        viewModel.summaryHistory.observe(this) { history ->
            Log.d(TAG, "Observed summary history list size: ${history.size}")
            if (history.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvSummaries.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvSummaries.visibility = View.VISIBLE
                adapter.submitList(history)
            }
        }

        viewModel.loadSummaryHistory()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
