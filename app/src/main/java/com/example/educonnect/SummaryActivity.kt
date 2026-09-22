package com.example.educonnect

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.educonnect.databinding.ActivitySummaryBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader

@AndroidEntryPoint
class SummaryActivity : AppCompatActivity() {

    private val TAG = "SummaryActivity"
    private lateinit var binding: ActivitySummaryBinding
    private val viewModel: SummaryViewModel by viewModels()

    private var userGrade: String = "10"
    private var imageBase64: String? = null

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.d(TAG, "File picked: $uri")
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        val content = reader.readText()
                        binding.etSourceText.setText(content)
                        // Clear any previous image state
                        binding.ivPreview.visibility = View.GONE
                        imageBase64 = null
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to read file text: ${e.localizedMessage}")
                Toast.makeText(this, "Failed to read file content", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.d(TAG, "Image picked: $uri")
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.ivPreview.setImageBitmap(bitmap)
                    binding.ivPreview.visibility = View.VISIBLE
                    
                    // Compress and Encode to Base64
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                    val bytes = outputStream.toByteArray()
                    imageBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
                    
                    // Clear text field to avoid confusion
                    binding.etSourceText.setText("")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load/encode image: ${e.localizedMessage}")
                Toast.makeText(this, "Failed to load image file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userGrade = intent.getStringExtra("USER_GRADE") ?: "10"
        Log.d(TAG, "Initialized SummaryActivity for Grade: $userGrade")

        setupClickListeners()
        observeViewModel()

        val existingSummary = intent.getSerializableExtra("EXISTING_SUMMARY") as? Summary
        if (existingSummary != null) {
            Log.d(TAG, "Loading existing summary for viewing: ${existingSummary.summaryId}")
            viewModel.loadPastSummary(existingSummary)
        }
    }

    private fun setupClickListeners() {
        binding.btnPickFile.setOnClickListener {
            pickFileLauncher.launch("text/plain")
        }

        binding.btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnGenerateSummary.setOnClickListener {
            val subject = binding.etSubjectInput.text.toString().trim()
            if (subject.isEmpty()) {
                Toast.makeText(this, "Please specify a subject", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.ivPreview.visibility == View.VISIBLE && imageBase64 != null) {
                Log.d(TAG, "Dispatching multimodal image summary job")
                viewModel.generateImageSummary(imageBase64!!, "image/jpeg", subject, userGrade)
            } else {
                val rawText = binding.etSourceText.text.toString().trim()
                if (rawText.isEmpty()) {
                    Toast.makeText(this, "Please paste textbook text or upload a document/image", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Log.d(TAG, "Dispatching standard raw text summary job")
                viewModel.generateSummary(rawText, subject, userGrade)
            }
        }

        binding.btnNewSummary.setOnClickListener {
            viewModel.reset()
            binding.resultContainer.visibility = View.GONE
            binding.inputContainer.visibility = View.VISIBLE
            binding.etSourceText.setText("")
            binding.etSubjectInput.setText("")
            binding.ivPreview.visibility = View.GONE
            imageBase64 = null
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnGenerateSummary.isEnabled = !loading
            binding.btnPickFile.isEnabled = !loading
            binding.btnPickImage.isEnabled = !loading
        }

        viewModel.errorMessage.observe(this) { msg ->
            msg?.let {
                Log.e(TAG, "Error state observed: $it")
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.summary.observe(this) { sum ->
            if (sum != null) {
                Log.d(TAG, "Summary data loaded or produced: ${sum.title}")
                binding.inputContainer.visibility = View.GONE
                binding.resultContainer.visibility = View.VISIBLE

                binding.tvSummaryTitle.text = sum.title
                binding.tvKeyPoints.text = sum.keyPoints.joinToString("\n• ", prefix = "• ")
                binding.tvSummaryBody.text = sum.summaryText
            }
        }
    }
}
