package com.example.educonnect.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToCourseDetail: (String) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()

    // Fetch profile on launch
    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUserProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EduConnect Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Welcome Section
            userProfile?.let { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Welcome, ${user.name.ifBlank { "Student" }}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Grade ${user.grade.ifBlank { "12" }} Student",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Compulsory Subjects Section
                Text(
                    text = "Compulsory Subjects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                SubjectCard(title = "Home Language", name = user.homeLanguage.ifBlank { "English Home Language" }, icon = Icons.Default.MenuBook) {
                    onNavigateToCourseDetail("hl_${(user.homeLanguage.ifBlank { "English Home Language" }).hashCode()}")
                }
                SubjectCard(title = "First Additional Language", name = user.firstAdditionalLanguage.ifBlank { "Afrikaans First Additional Language" }, icon = Icons.Default.MenuBook) {
                    onNavigateToCourseDetail("fal_${(user.firstAdditionalLanguage.ifBlank { "Afrikaans First Additional Language" }).hashCode()}")
                }
                SubjectCard(title = "Mathematics", name = user.mathematics.ifBlank { "Mathematics" }, icon = Icons.Default.School) {
                    onNavigateToCourseDetail("math_${(user.mathematics.ifBlank { "Mathematics" }).hashCode()}")
                }
                SubjectCard(title = "Life Orientation", name = "Life Orientation (LO)", icon = Icons.Default.Star) {
                    onNavigateToCourseDetail("lo_subject")
                }

                // Elective Subjects Section
                Text(
                    text = "Elective Subjects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                val electivesToShow = if (user.electives.isEmpty()) {
                    listOf("Physical Sciences", "Accounting", "Geography")
                } else {
                    user.electives
                }

                electivesToShow.forEachIndexed { index, elective ->
                    SubjectCard(title = "Elective", name = elective, icon = Icons.Default.School) {
                        onNavigateToCourseDetail("elective_${index}_${elective.hashCode()}")
                    }
                }
            } ?: run {
                // Loading state if profile hasn't loaded yet
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SubjectCard(title: String, name: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Text(text = name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
            Text(text = "▶", color = Color.Gray, fontSize = 14.sp)
        }
    }
}
