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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.ui.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val user by authViewModel.userState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(user?.name ?: "Zanele Mkhize") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Top Banner card matching screen 8
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E6091))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Profile Avatar Photo circle with overlay bubble
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👩‍🎓", fontSize = 54.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF40916C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📸", fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = user?.name ?: "Zanele Mkhize", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = user?.email ?: "zanele.mkhize@email.com", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditing) {
            // Profile Editing form panel content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Edit Personal Info", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            authViewModel.updateProfile(newName, "")
                            isEditing = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40916C)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                    OutlinedButton(
                        onClick = { isEditing = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        } else {
            // Options list table rows view
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                ProfileOptionRow(icon = "👤", title = "Personal Information") { isEditing = true }
                HorizontalDivider()
                ProfileOptionRow(icon = "🔒", title = "Change Password") {}
                HorizontalDivider()
                ProfileOptionRow(icon = "🛡️", title = "Privacy Settings") {}
                HorizontalDivider()
                ProfileOptionRow(icon = "❓", title = "Help & Support") {}
                HorizontalDivider()
                ProfileOptionRow(icon = "➡️", title = "Logout", titleColor = Color.Red) { onLogout() }
            }
        }

        // Gamification metrics indicators layout summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔥 ${user?.streak ?: 3} Days", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65F2B))
                    Text(text = "Streak Count", fontSize = 12.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🏆 ${user?.points ?: 120} XP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E6091))
                    Text(text = "Total Points", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ProfileOptionRow(
    icon: String,
    title: String,
    titleColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = titleColor)
        }
        Text(text = "▶", color = Color.LightGray, fontSize = 14.sp)
    }
}
