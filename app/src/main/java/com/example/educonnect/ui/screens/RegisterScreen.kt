package com.example.educonnect.ui.screens

import android.accounts.AccountManager
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.data.local.entities.User
import com.example.educonnect.ui.viewmodel.AuthUiState
import com.example.educonnect.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Form States
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("10") }
    var subjectNumber by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Compulsory Subjects States
    val languages = listOf("English", "Afrikaans", "isiZulu", "isiXhosa", "isiNdebele", "Sepedi", "Sesotho", "Setswana", "Siswati", "Tshivenda", "Xitsonga")
    var homeLanguage by remember { mutableStateOf(languages[0]) }
    var firstAdditionalLanguage by remember { mutableStateOf(languages[1]) }
    var hlExpanded by remember { mutableStateOf(false) }
    var falExpanded by remember { mutableStateOf(false) }
    var mathematics by remember { mutableStateOf("Mathematics") }

    // Elective Subjects States
    val selectedElectives = remember { mutableStateListOf<String>() }

    val electiveStreams = mapOf(
        "Math, Science & Technology" to listOf(
            "Physical Sciences", "Life Sciences", "Information Technology", "Engineering Graphics and Design",
            "Agricultural Sciences", "Agricultural Technology", "Agricultural Management Practices",
            "Technical Mathematics", "Technical Sciences", "Civil Technology", "Electrical Technology", "Mechanical Technology"
        ),
        "Commercial & Business" to listOf("Accounting", "Business Studies", "Economics", "Computer Applications Technology (CAT)"),
        "Humanities & Social Sciences" to listOf(
            "Geography", "History", "Religion Studies", "Consumer Studies", "Hospitality Studies", "Tourism",
            "Dramatic Arts", "Visual Arts", "Design", "Music", "Dance Studies"
        )
    )

    val googleAccountPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val accountName = result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            if (accountName != null) {
                email = accountName
                name = accountName.substringBefore("@").replace(".", " ")
                viewModel.loginWithGoogle(email = accountName, name = name)
            } else {
                viewModel.loginWithGoogle()
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
            viewModel.resetState()
        } else if (uiState is AuthUiState.Error) {
            Toast.makeText(context, (uiState as AuthUiState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Registration", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: Account Information
                Text("Account Details", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedButton(
                    onClick = {
                        try {
                            val intent = AccountManager.newChooseAccountIntent(
                                null, null, arrayOf("com.google"), null, null, null, null
                            )
                            googleAccountPickerLauncher.launch(intent)
                        } catch (_: Exception) {
                            viewModel.loginWithGoogle()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sign up with Google")
                }

                HorizontalDivider()

                // Section 2: Personal Information
                Text("Personal Information", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = surname, onValueChange = { surname = it }, label = { Text("Surname") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = subjectNumber, onValueChange = { subjectNumber = it }, label = { Text("South African ID / Student Number") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                // Grade Selector
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = {}
                ) {
                    OutlinedTextField(
                        value = "Grade $grade",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Grade") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                HorizontalDivider()

                // Section 3: Subject Selections
                Text("Subject Selections", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

                // Home Language Dropdown
                ExposedDropdownMenuBox(
                    expanded = hlExpanded,
                    onExpandedChange = { hlExpanded = !hlExpanded }
                ) {
                    OutlinedTextField(
                        value = homeLanguage,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Home Language (HL)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = hlExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = hlExpanded,
                        onDismissRequest = { hlExpanded = false }
                    ) {
                        languages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang) },
                                onClick = {
                                    homeLanguage = lang
                                    hlExpanded = false
                                }
                            )
                        }
                    }
                }

                // First Additional Language Dropdown
                ExposedDropdownMenuBox(
                    expanded = falExpanded,
                    onExpandedChange = { falExpanded = !falExpanded }
                ) {
                    OutlinedTextField(
                        value = firstAdditionalLanguage,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("First Additional Language (FAL)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = falExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = falExpanded,
                        onDismissRequest = { falExpanded = false }
                    ) {
                        languages.filter { it != homeLanguage }.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang) },
                                onClick = {
                                    firstAdditionalLanguage = lang
                                    falExpanded = false
                                }
                            )
                        }
                    }
                }

                // Mathematics Dropdown
                OutlinedTextField(
                    value = mathematics,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Mathematics / Mathematical Literacy") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Electives Section
                Text("Select Exactly 3 Elective Subjects", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)

                electiveStreams.forEach { (streamName, subjects) ->
                    Text(streamName, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        subjects.forEach { subject ->
                            val isSelected = selectedElectives.contains(subject)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        selectedElectives.remove(subject)
                                    } else if (selectedElectives.size < 3) {
                                        selectedElectives.add(subject)
                                    }
                                },
                                label = { Text(subject, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val user = User(
                            name = name,
                            surname = surname,
                            grade = grade,
                            subjectNumber = subjectNumber,
                            homeLanguage = homeLanguage,
                            firstAdditionalLanguage = firstAdditionalLanguage,
                            mathematics = mathematics,
                            electives = selectedElectives.toList(),
                            lifeOrientation = true
                        )
                        viewModel.register(email.trim(), password, user)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = uiState !is AuthUiState.Loading
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Complete Registration", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
