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

@Composable
fun BudgetScreen() {
    var selectedPeriod by remember { mutableStateOf("This Month") }
    val periods = listOf("This Week", "This Month", "This Year")

    // Mock budget data per category
    data class BudgetCategory(val name: String, val spent: Float, val minGoal: Float, val maxGoal: Float)

    val categories = listOf(
        BudgetCategory("Stationery & Books", 120f, 50f, 150f),
        BudgetCategory("Transport", 350f, 200f, 400f),
        BudgetCategory("Uniform & Apparel", 250f, 100f, 300f),
        BudgetCategory("Snacks & Lunch", 180f, 100f, 150f), // Over max goal
        BudgetCategory("Tech & Gadgets", 400f, 200f, 500f)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(text = "Student Budget & Financial Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
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

        // Graph / Spending per Category section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "📊 Spending vs Goals ($selectedPeriod)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    categories.forEach { cat ->
                        val isOverspent = cat.spent > cat.maxGoal
                        val progress = (cat.spent / cat.maxGoal).coerceIn(0f, 1f)

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = cat.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text(
                                    text = "R${cat.spent.toInt()} (Goal: R${cat.minGoal.toInt()} - R${cat.maxGoal.toInt()})",
                                    fontSize = 12.sp,
                                    color = if (isOverspent) Color.Red else Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = if (isOverspent) Color.Red else Color(0xFF40916C),
                                trackColor = Color(0xFFE9ECEF)
                            )
                            if (isOverspent) {
                                Text(text = "⚠️ Overspent by R${(cat.spent - cat.maxGoal).toInt()}!", fontSize = 11.sp, color = Color.Red, fontWeight = FontWeight.Bold)
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
                    Text(text = "🎯 Monthly Goal Adherence", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "You successfully stayed within your budget limits in 4 out of 5 categories this past month. Keep tracking your expenses to maintain healthy financial habits!",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { 0.8f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = Color(0xFF1E6091),
                        trackColor = Color(0xFFE9ECEF)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "80% Goal Adherence Rate", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E6091))
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
                    Text(text = "🏆 Financial Badges & Rewards", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        BadgeItem(emoji = "🥇", title = "Budget Master", unlocked = true)
                        BadgeItem(emoji = "🔥", title = "7-Day Streak", unlocked = true)
                        BadgeItem(emoji = "🌟", title = "Savings Guru", unlocked = false)
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
fun BadgeItem(emoji: String, title: String, unlocked: Boolean) {
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
