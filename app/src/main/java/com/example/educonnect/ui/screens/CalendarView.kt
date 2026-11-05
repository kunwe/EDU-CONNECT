package com.example.educonnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    dueDays: List<Int> = emptyList() // List of day numbers that have assignments in current month
) {
    var calendarInstance by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDay by remember { mutableStateOf(calendarInstance.get(Calendar.DAY_OF_MONTH)) }

    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val monthTitle = monthFormat.format(calendarInstance.time)

    val currentYear = calendarInstance.get(Calendar.YEAR)
    val currentMonth = calendarInstance.get(Calendar.MONTH)
    val today = Calendar.getInstance()
    val isCurrentMonthAndYear = today.get(Calendar.YEAR) == currentYear && today.get(Calendar.MONTH) == currentMonth
    val todayDate = today.get(Calendar.DAY_OF_MONTH)

    // Calculate days in month and starting day offset
    val tempCal = calendarInstance.clone() as Calendar
    tempCal.set(Calendar.DAY_OF_MONTH, 1)
    val maxDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val dayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK)
    val startDayOffset = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - Calendar.MONDAY

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = monthTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                val newCal = calendarInstance.clone() as Calendar
                                newCal.add(Calendar.MONTH, -1)
                                calendarInstance = newCal
                            }
                            .padding(8.dp)
                    ) {
                        Text(text = "◀", color = Color(0xFF1E6091), fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                val newCal = calendarInstance.clone() as Calendar
                                newCal.add(Calendar.MONTH, 1)
                                calendarInstance = newCal
                            }
                            .padding(8.dp)
                    ) {
                        Text(text = "▶", color = Color(0xFF1E6091), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Days of week
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val emptyPrefix = List(startDayOffset) { 0 }
            val days = (1..maxDays).toList()
            val allGridItems = emptyPrefix + days
            val rows = allGridItems.chunked(7)

            rows.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    row.forEach { day ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable(enabled = day != 0) {
                                    selectedDay = day
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (day != 0) {
                                val isToday = isCurrentMonthAndYear && day == todayDate
                                val isSelected = day == selectedDay
                                val hasDue = dueDays.contains(day)

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isSelected -> Color(0xFF40916C)
                                                    isToday -> Color(0xFF1E6091)
                                                    else -> Color.Transparent
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day.toString(),
                                            fontSize = 14.sp,
                                            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isToday || isSelected) Color.White else Color.Black
                                        )
                                    }
                                    if (hasDue) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 2.dp)
                                                .size(4.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE65F2B))
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (row.size < 7) {
                        repeat(7 - row.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
