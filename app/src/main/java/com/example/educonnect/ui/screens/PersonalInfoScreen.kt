package com.example.educonnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.data.local.entities.User
import com.example.educonnect.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val userState by authViewModel.userState.collectAsState()
    val user = userState ?: return

    var legalName by remember { mutableStateOf(user.legalName) }
    var displayName by remember { mutableStateOf(user.name) }
    var primaryEmail by remember { mutableStateOf(user.email) }
    var backupEmail by remember { mutableStateOf(user.backupEmail) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var dob by remember { mutableStateOf(user.dateOfBirth) }
    var bio by remember { mutableStateOf(user.bio) }
    var jobTitle by remember { mutableStateOf(user.jobTitle) }

    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Information") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Names", fontWeight = FontWeight.Bold, color = Color(0xFF1E6091))
            OutlinedTextField(value = legalName, onValueChange = { legalName = it }, label = { Text("Legal Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = displayName, onValueChange = { displayName = it }, label = { Text("Display Name") }, modifier = Modifier.fillMaxWidth())

            Text(text = "Contact Details", fontWeight = FontWeight.Bold, color = Color(0xFF1E6091), modifier = Modifier.padding(top = 8.dp))
            OutlinedTextField(value = primaryEmail, onValueChange = { primaryEmail = it }, label = { Text("Primary Email") }, modifier = Modifier.fillMaxWidth(), enabled = false)
            OutlinedTextField(value = backupEmail, onValueChange = { backupEmail = it }, label = { Text("Backup Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())

            Text(text = "Additional Info", fontWeight = FontWeight.Bold, color = Color(0xFF1E6091), modifier = Modifier.padding(top = 8.dp))
            OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = jobTitle, onValueChange = { jobTitle = it }, label = { Text("Job Title / Degree") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Changes")
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Changes") },
            text = {
                Column {
                    Text("Please confirm you want to update your profile details.")
                    if (backupEmail != user.backupEmail || phoneNumber != user.phoneNumber) {
                        Text(
                            text = "\nNote: You will need to verify any new contact details via the link sent to your device.",
                            color = Color(0xFFE65F2B),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val updated = user.copy(
                        legalName = legalName,
                        name = displayName,
                        backupEmail = backupEmail,
                        phoneNumber = phoneNumber,
                        dateOfBirth = dob,
                        bio = bio,
                        jobTitle = jobTitle
                    )
                    authViewModel.updateExpandedProfile(updated)
                    showConfirmDialog = false
                    onBack()
                }) {
                    Text("Confirm & Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Cancel") }
            }
        )
    }
}
