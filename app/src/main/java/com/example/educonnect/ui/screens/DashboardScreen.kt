package com.example.educonnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.data.local.entities.Assignment
import com.example.educonnect.data.local.entities.Subject
import com.example.educonnect.ui.viewmodel.ModuleViewModel

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.educonnect.R

import com.example.educonnect.ui.viewmodel.AuthViewModel

@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel,
    moduleViewModel: ModuleViewModel,
    onNavigateToCourses: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val subjects by moduleViewModel.subjects.collectAsState()
    val assignments by moduleViewModel.assignments.collectAsState()
    val user by authViewModel.userState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Profile Header Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.edu_logo),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Hello, ${user?.name ?: "User"} 👋", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Keep learning!", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box {
                        Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(28.dp))
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .align(Alignment.TopEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "3", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Profile Image placeholder bubble circle
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE9ECEF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👩‍🎓", fontSize = 24.sp)
                    }
                }
            }
        }

        // Search Bar Textbox field
        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search courses, assignments...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF1F3F5),
                    focusedContainerColor = Color(0xFFF1F3F5)
                )
            )
        }

        // My Courses Header Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "My Courses", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "View all",
                    color = Color(0xFF1E6091),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToCourses() }
                )
            }
        }

        // Horizontal Course Scroll List Cards
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(subjects) { subject ->
                    CourseCard(subject = subject, onClick = { onNavigateToDetail(subject.id) })
                }
            }
        }

        // Recent Assignments Section Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Recent Assignments", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "View all", color = Color(0xFF1E6091), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Table Rows List
        items(assignments) { assignment ->
            AssignmentRow(assignment = assignment, onToggle = { moduleViewModel.toggleAssignmentCompleted(assignment) })
        }
    }
}

@Composable
fun CourseCard(subject: Subject, onClick: () -> Unit) {
    // Choose dynamic beautiful background color boxes based on module ID parameters
    val containerColor = when (subject.id) {
        "math_101" -> Color(0xFF1E6091)
        "lit_202" -> Color(0xFF40916C)
        "sci_303" -> Color(0xFF743059)
        else -> Color(0xFFE65F2B)
    }

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(170.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.List, contentDescription = null, tint = Color.White)
            }

            Column {
                Text(text = subject.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = subject.teacherName, fontSize = 12.sp, color = Color.Gray)
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${subject.progress}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = containerColor)
                    Text(text = "📊", fontSize = 11.sp)
                }
                LinearProgressIndicator(
                    progress = subject.progress / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    color = containerColor,
                    trackColor = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun AssignmentRow(assignment: Assignment, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F9FA))
            .clickable { onToggle() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = if (assignment.attachmentType == "pdf") "📄" else "📝", fontSize = 20.sp)
            Column {
                Text(
                    text = assignment.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (assignment.isCompleted) Color.Gray else Color.Black
                )
                if (assignment.attachmentName.isNotEmpty()) {
                    Text(text = "📎 ${assignment.attachmentName} (${assignment.attachmentType})", fontSize = 11.sp, color = Color(0xFF1E6091))
                }
            }
        }

        val flagColor = when (assignment.dueDate) {
            "Due: Tomorrow" -> Color(0xFFE65F2B)
            "Due: 3 May" -> Color(0xFF40916C)
            else -> Color.Gray
        }

        Text(text = assignment.dueDate, fontSize = 12.sp, color = flagColor, fontWeight = FontWeight.Medium)
    }
}
