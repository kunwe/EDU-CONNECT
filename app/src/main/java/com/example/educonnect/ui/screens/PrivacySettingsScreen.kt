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
import com.example.educonnect.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val userState by authViewModel.userState.collectAsState()
    val user = userState ?: return

    var visibility by remember { mutableStateOf(user.profileVisibility) }
    var searchDiscoverable by remember { mutableStateOf(user.searchDiscoverable) }
    var analytics by remember { mutableStateOf(user.analyticsOptIn) }
    var ads by remember { mutableStateOf(user.adsOptIn) }
    var statusEnabled by remember { mutableStateOf(user.activityStatusEnabled) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy & Data") },
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Visibility
            PrivacySection(
                title = "Profile Visibility",
                description = "Choose who can see your course progress and badges. 'Private' hides your profile from all searches."
            ) {
                Column {
                    listOf("Public", "Friends Only", "Private").forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(selected = visibility == option, onClick = { visibility = option })
                            Text(text = option, fontSize = 15.sp)
                        }
                    }
                }
            }

            // Discoverability
            PrivacyToggle(
                title = "Search Discoverability",
                description = "Allow other users to find your profile using your email or phone number.",
                checked = searchDiscoverable,
                onCheckedChange = { searchDiscoverable = it }
            )

            // Activity Status
            PrivacyToggle(
                title = "Activity Status",
                description = "Show your 'Last Seen' timestamp and current online status to your connections.",
                checked = statusEnabled,
                onCheckedChange = { statusEnabled = it }
            )

            // Data Sharing
            PrivacySection(
                title = "Data & Personalization",
                description = "Manage how your data is used for improving the app experience."
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Personalized Analytics", fontSize = 15.sp)
                        Switch(checked = analytics, onCheckedChange = { analytics = it })
                    }
                    Text(
                        text = "Helps us understand how you use EduConnect to provide better remedial quiz suggestions.",
                        fontSize = 12.sp, color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Third-Party Ads", fontSize = 15.sp)
                        Switch(checked = ads, onCheckedChange = { ads = it })
                    }
                    Text(
                        text = "Enable cookies for tailored educational resource recommendations.",
                        fontSize = 12.sp, color = Color.Gray
                    )
                }
            }

            Button(
                onClick = {
                    val updated = user.copy(
                        profileVisibility = visibility,
                        searchDiscoverable = searchDiscoverable,
                        analyticsOptIn = analytics,
                        adsOptIn = ads,
                        activityStatusEnabled = statusEnabled
                    )
                    authViewModel.updateExpandedProfile(updated)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Privacy Settings")
            }
        }
    }
}

@Composable
fun PrivacySection(title: String, description: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF1E6091))
        Text(text = description, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}

@Composable
fun PrivacyToggle(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    PrivacySection(title = title, description = description) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
