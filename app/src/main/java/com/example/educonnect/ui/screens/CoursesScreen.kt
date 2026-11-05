package com.example.educonnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.data.local.entities.Subject
import com.example.educonnect.ui.viewmodel.ModuleViewModel

@Composable
fun CoursesScreen(
    moduleViewModel: ModuleViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val subjects by moduleViewModel.subjects.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "My Courses", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "🔍", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search courses...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF8F9FA),
                    focusedContainerColor = Color(0xFFF8F9FA)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(subjects) { subject ->
                    CourseListItem(subject = subject, onClick = { onNavigateToDetail(subject.id) })
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Color(0xFF1E6091),
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Course")
        }
    }

    if (showAddDialog) {
        AddEditCourseDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, teacher, desc ->
                moduleViewModel.addSubject(title, teacher, desc)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CourseListItem(subject: Subject, onClick: () -> Unit) {
    val accentColor = when (subject.id) {
        "math_101" -> Color(0xFF1E6091)
        "lit_202" -> Color(0xFF40916C)
        "sci_303" -> Color(0xFF743059)
        "hist_404" -> Color(0xFFE65F2B)
        else -> Color(0xFF1E6091)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.List, contentDescription = null, tint = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = subject.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = subject.teacherName, fontSize = 13.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { subject.progress / 100f },
                    modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = accentColor,
                    trackColor = Color(0xFFE9ECEF)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${subject.progress}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = "▶", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun AddEditCourseDialog(
    initialSubject: Subject? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialSubject?.title ?: "") }
    var teacher by remember { mutableStateOf(initialSubject?.teacherName ?: "") }
    var description by remember { mutableStateOf(initialSubject?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (initialSubject == null) "Add New Course" else "Edit Course") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Course Title") })
                OutlinedTextField(value = teacher, onValueChange = { teacher = it }, label = { Text("Teacher Name") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.height(100.dp))
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(title, teacher, description) }) {
                Text(if (initialSubject == null) "Create" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
