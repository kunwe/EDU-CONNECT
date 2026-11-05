package com.example.educonnect.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.data.local.entities.Assignment
import com.example.educonnect.data.local.entities.GradeRecord
import com.example.educonnect.data.local.entities.Material
import com.example.educonnect.ui.viewmodel.ModuleViewModel

@Composable
fun CourseDetailScreen(
    subjectId: String,
    moduleViewModel: ModuleViewModel,
    onBack: () -> Unit,
    onTakeQuiz: () -> Unit
) {
    val subjects by moduleViewModel.subjects.collectAsState()
    val materials by moduleViewModel.materials.collectAsState()
    val assignments by moduleViewModel.assignments.collectAsState()
    val grades by moduleViewModel.grades.collectAsState()
    
    val subject = subjects.find { it.id == subjectId } ?: return
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Materials", "Assignments", "Grades")

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(subjectId) {
        moduleViewModel.loadCourseDetails(subjectId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E6091))
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "← Back", color = Color.White, modifier = Modifier.clickable { onBack() })
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = subject.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = subject.teacherName, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    }

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${subject.progress}%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Average", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }

        // Interactive Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Color(0xFF1E6091),
            edgePadding = 16.dp,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        HorizontalDivider()

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> OverviewTab(subject, onTakeQuiz)
                1 -> MaterialsTab(materials)
                2 -> AssignmentsTab(assignments.filter { it.subjectId == subjectId })
                3 -> GradesTab(grades)
            }

            if (selectedTab != 0) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    containerColor = Color(0xFF40916C),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }

    if (showEditDialog) {
        AddEditCourseDialog(
            initialSubject = subject,
            onDismiss = { showEditDialog = false },
            onConfirm = { title, teacher, desc ->
                moduleViewModel.updateSubject(subject.id, title, teacher, desc)
                showEditDialog = false
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Course") },
            text = { Text("Are you sure you want to delete '${subject.title}'? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        moduleViewModel.deleteSubject(subject)
                        showDeleteConfirm = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }

    if (showAddDialog) {
        AddAssetDialog(
            tabIndex = selectedTab,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, extra, dateOrScore ->
                when (selectedTab) {
                    1 -> moduleViewModel.addMaterial(subjectId, name, extra)
                    2 -> moduleViewModel.addAssignment(subjectId, name, dateOrScore, "file", extra)
                    3 -> moduleViewModel.addGrade(subjectId, name, dateOrScore.toIntOrNull() ?: 0)
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
fun OverviewTab(subject: com.example.educonnect.data.local.entities.Subject, onTakeQuiz: () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "About this course", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            text = subject.description.ifEmpty { "Tailored study modules to enhance mastery scores." },
            fontSize = 14.sp, color = Color.DarkGray
        )
        InfoRow(label = "👤 Teacher", value = subject.teacherName)
        InfoRow(label = "🏷️ Category", value = subject.category)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onTakeQuiz,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Take Adaptive Quiz")
        }
    }
}

@Composable
fun MaterialsTab(materials: List<Material>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(materials) { material ->
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFF8F9FA)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = when(material.fileType) { "pdf" -> "📄" "word" -> "📝" "png" -> "🖼️" else -> "🗒️" }, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = material.title, fontWeight = FontWeight.Bold)
                    Text(text = material.fileType.uppercase(), fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun AssignmentsTab(assignments: List<Assignment>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(assignments) { assignment ->
            AssignmentRow(assignment = assignment, onToggle = {})
        }
    }
}

@Composable
fun GradesTab(grades: List<GradeRecord>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(grades) { grade ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = grade.title)
                Text(text = "${grade.scorePercentage}%", fontWeight = FontWeight.Bold, color = if (grade.scorePercentage >= 50) Color(0xFF40916C) else Color.Red)
            }
            HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetDialog(tabIndex: Int, onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var extra by remember { mutableStateOf("pdf") }
    var dueDate by remember { mutableStateOf("Due: Next Week") }
    var score by remember { mutableStateOf("") }
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            val fileName = cursor?.use { c ->
                val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (c.moveToFirst() && nameIndex != -1) c.getString(nameIndex) else null
            } ?: uri.lastPathSegment ?: "Document"

            name = fileName
            val ext = fileName.substringAfterLast('.').lowercase()
            if (ext in listOf("pdf", "txt", "word", "png", "docx", "doc")) {
                extra = if (ext == "docx" || ext == "doc") "word" else ext
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add ${if (tabIndex == 1) "Material" else if (tabIndex == 2) "Assignment" else "Grade"}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(if (tabIndex == 2) "Assignment Title" else "Title / File Name") })
                if (tabIndex == 2) {
                    OutlinedTextField(value = dueDate, onValueChange = { dueDate = it }, label = { Text("Due Date (e.g., Due: 15 June)") })
                }
                if (tabIndex == 1 || tabIndex == 2) {
                    Button(
                        onClick = { filePickerLauncher.launch(arrayOf("*/*")) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E6091))
                    ) {
                        Text("📁 Choose File from Internal Storage")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Select Type:")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("txt", "pdf", "word", "png").forEach { type ->
                            FilterChip(selected = extra == type, onClick = { extra = type }, label = { Text(type) })
                        }
                    }
                } else if (tabIndex == 3) {
                    OutlinedTextField(value = score, onValueChange = { score = it }, label = { Text("Score %") })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name, extra, if (tabIndex == 2) dueDate else score) },
                enabled = name.isNotBlank()
            ) {
                Text("Add")
            }
        }
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
    }
}
