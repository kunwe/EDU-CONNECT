package com.example.educonnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.ui.viewmodel.QuizViewModel

@Composable
fun QuizPlayerScreen(
    quizId: String,
    quizViewModel: QuizViewModel,
    onFinish: () -> Unit
) {
    val questions by quizViewModel.currentQuestions.collectAsState()
    val currentIndex by quizViewModel.currentQuestionIndex.collectAsState()
    val isFinished by quizViewModel.isQuizFinished.collectAsState()
    val score by quizViewModel.score.collectAsState()

    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var isCorrectFeedback by remember { mutableStateOf(false) }

    LaunchedEffect(quizId) {
        quizViewModel.startQuiz(quizId)
    }

    if (isFinished) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🎉 Quiz Complete!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Your score: $score / ${questions.size}", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onFinish, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) {
                Text(text = "Return to Course")
            }
        }
        return
    }

    if (questions.isEmpty() || currentIndex >= questions.size) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val question = questions[currentIndex]
    val options = question.optionsString.split(",").filter { it.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Question ${currentIndex + 1} of ${questions.size}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE65F2B).copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "⏱️ 00:45", color = Color(0xFFE65F2B), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / questions.size },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = Color(0xFF1E6091),
            trackColor = Color(0xFFE9ECEF)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = question.questionText, fontSize = 20.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp)

        Spacer(modifier = Modifier.height(12.dp))

        if (question.questionType == "Written Response") {
            var writtenText by remember { mutableStateOf("") }
            OutlinedTextField(
                value = writtenText,
                onValueChange = { writtenText = it },
                label = { Text("Type your answer here") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    quizViewModel.submitAnswer(writtenText) { correct, explanation ->
                        isCorrectFeedback = correct
                        feedbackMessage = explanation
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Submit Answer")
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                val itemsToRender = if (options.isEmpty()) listOf("True", "False") else options
                itemsToRender.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8F9FA))
                            .clickable {
                                quizViewModel.submitAnswer(option) { correct, explanation ->
                                    isCorrectFeedback = correct
                                    feedbackMessage = explanation
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = option, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        feedbackMessage?.let { msg ->
            LaunchedEffect(currentIndex) {
                feedbackMessage = null
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isCorrectFeedback) Color(0xFFD8F3DC) else Color(0xFFFAD2E1))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = if (isCorrectFeedback) "🎯 Correct!" else "❌ Incorrect", fontWeight = FontWeight.Bold, color = if (isCorrectFeedback) Color(0xFF40916C) else Color.Red)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = msg, fontSize = 13.sp)
                }
            }
        }
    }
}
