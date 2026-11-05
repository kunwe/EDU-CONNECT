package com.example.educonnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

data class SubjectStudyTime(val name: String, val hoursSpent: Float, val minGoalHours: Float, val maxGoalHours: Float)

@Composable
fun StudyTrackerScreen() {
    var selectedPeriod by remember { mutableStateOf("This Week") }
    val periods = listOf("This Week", "This Month", "This Term")

    // Dynamic data changing based on selected period
    val (subjectsStudy, adherenceRate, summaryText) = when (selectedPeriod) {
        "This Month" -> Triple(
            listOf(
                SubjectStudyTime("Mathematics 101", 28.0f, 16.0f, 32.0f),
                SubjectStudyTime("English Literature", 22.5f, 12.0f, 24.0f),
                SubjectStudyTime("Physical Science", 30.0f, 20.0f, 32.0f),
                SubjectStudyTime("History", 14.0f, 12.0f, 20.0f),
                SubjectStudyTime("Life Orientation", 10.0f, 8.0f, 16.0f)
            ),
            0.92f,
            "You successfully maintained steady study progress across all subjects throughout this month. Excellent consistency!"
        )
        "This Term" -> Triple(
            listOf(
                SubjectStudyTime("Mathematics 101", 75.0f, 50.0f, 90.0f),
                SubjectStudyTime("English Literature", 60.0f, 40.0f, 70.0f),
                SubjectStudyTime("Physical Science", 82.0f, 60.0f, 90.0f),
                SubjectStudyTime("History", 45.0f, 40.0f, 60.0f),
                SubjectStudyTime("Life Orientation", 30.0f, 25.0f, 45.0f)
            ),
            0.90f,
            "Term-to-date study hours show exceptional dedication, keeping you well on track for final examinations."
        )
        else -> Triple( // This Week
            listOf(
                SubjectStudyTime("Mathematics 101", 6.5f, 4.0f, 8.0f),
                SubjectStudyTime("English Literature", 5.0f, 3.0f, 6.0f),
                SubjectStudyTime("Physical Science", 7.2f, 5.0f, 8.0f),
                SubjectStudyTime("History", 2.5f, 3.0f, 5.0f), // Below minimum goal
                SubjectStudyTime("Life Orientation", 3.0f, 2.0f, 4.0f)
            ),
            0.85f,
            "You successfully met or exceeded your study time goals in 4 out of 5 subjects this week. History needs a bit more focus!"
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(text = "Study Time & Subject Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))

            // Period selector
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                periods.forEach { period ->
                    FilterChip(
                        selected = selectedPeriod == period,
                        onClick = { selectedPeriod = period },
                        label = { Text(period) }
                    )
                }
            }
        }

        // Graph / Time Spent per Subject section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "⏱️ Study Time vs Goals ($selectedPeriod)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    subjectsStudy.forEach { sub ->
                        val isBelowGoal = sub.hoursSpent < sub.minGoalHours
                        val progress = (sub.hoursSpent / sub.maxGoalHours).coerceIn(0f, 1f)

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = sub.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text(
                                    text = "${sub.hoursSpent} hrs (Goal: ${sub.minGoalHours}h - ${sub.maxGoalHours}h)",
                                    fontSize = 12.sp,
                                    color = if (isBelowGoal) Color(0xFFE65F2B) else Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = if (isBelowGoal) Color(0xFFE65F2B) else Color(0xFF40916C),
                                trackColor = Color(0xFFE9ECEF)
                            )
                            if (isBelowGoal) {
                                Text(text = "⚠️ Needs more study time (Target min: ${sub.minGoalHours}h)", fontSize = 11.sp, color = Color(0xFFE65F2B), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Progress Visual Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "🎯 Overall Study Goal Adherence ($selectedPeriod)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = summaryText,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { adherenceRate },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = Color(0xFF1E6091),
                        trackColor = Color(0xFFE9ECEF)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "${(adherenceRate * 100).toInt()}% Study Goal Adherence", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E6091))
                }
            }
        }

        // Gamification Section (Badges & Rewards)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "🏆 Study Badges & Milestones", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StudyBadgeItem(emoji = "📐", title = "Math Whiz", unlocked = true)
                        StudyBadgeItem(emoji = "🔥", title = "7-Day Streak", unlocked = true)
                        StudyBadgeItem(emoji = "🏆", title = "Exam Ready", unlocked = false)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun StudyBadgeItem(emoji: String, title: String, unlocked: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(if (unlocked) Color(0xFFD8F3DC) else Color(0xFFE9ECEF)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (unlocked) Color.Black else Color.Gray)
        Text(text = if (unlocked) "Unlocked" else "Locked", fontSize = 10.sp, color = if (unlocked) Color(0xFF40916C) else Color.Gray)
    }
}
