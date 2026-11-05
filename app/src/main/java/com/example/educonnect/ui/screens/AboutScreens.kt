package com.example.educonnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TermsOfServiceScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "← Back", modifier = Modifier.clickable { onBack() }, color = Color(0xFF1E6091))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Terms of Service", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = """
                Last Updated: September 2026
                
                1. Acceptance of Terms
                By using EduConnect, you agree to abide by these terms. This application is designed to support your academic journey.
                
                2. Academic Integrity
                Users are expected to maintain the highest standards of academic honesty. Adaptive quizzes are for self-improvement and study purposes.
                
                3. Privacy and Data
                We value your privacy. Your data (modules, grades, attempts) is stored securely. We do not sell your personal information to third parties.
                
                4. User Conduct
                You agree not to use the app for any unlawful purposes or to transmit harmful code.
                
                5. Limitation of Liability
                EduConnect is provided "as is". We are not responsible for data loss or academic outcomes resulting from app usage.
            """.trimIndent(),
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun AboutEduConnectScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "← Back", modifier = Modifier.clickable { onBack() }, color = Color(0xFF1E6091))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "🎓📚", fontSize = 64.sp)
        Text(text = "EduConnect", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E6091))
        Text(text = "Version 1.0.0", fontSize = 12.sp, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Purpose",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "EduConnect is a comprehensive academic companion designed to empower students. Our purpose is to simplify course management, track academic progress automatically, and provide an adaptive learning environment through intelligent quiz engines. From organizing your timetable to analyzing your mastery trends, EduConnect is here to help you Connect, Learn, and Succeed.",
            fontSize = 15.sp,
            lineHeight = 24.sp
        )
    }
}


