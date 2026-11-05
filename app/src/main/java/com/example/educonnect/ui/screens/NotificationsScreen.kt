package com.example.educonnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.educonnect.ui.viewmodel.ModuleViewModel

import com.example.educonnect.data.model.NotificationItem

@Composable
fun NotificationsScreen(moduleViewModel: ModuleViewModel) {
    val allNotifications by moduleViewModel.notifications.collectAsState()
    val activeFilter by moduleViewModel.notificationFilter.collectAsState()
    var showCalendar by remember { mutableStateOf(true) }

    val filteredNotifications = when (activeFilter) {
        "Unread" -> allNotifications.filter { it.isUnread }
        "Announcements" -> allNotifications.filter { it.icon == "📢" }
        else -> allNotifications
    }

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
            Text(text = "Notifications", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { showCalendar = !showCalendar }) {
                Text(text = if (showCalendar) "📅 Hide Calendar" else "📅 Show Calendar", color = Color(0xFF1E6091))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar component (toggleable)
        if (showCalendar) {
            CalendarView(dueDays = listOf(22, 25)) // Mocking due dates on 22nd and 25th
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Interactive Filtering Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterTab(
                text = "All",
                isSelected = activeFilter == "All",
                onClick = { moduleViewModel.setNotificationFilter("All") }
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { moduleViewModel.setNotificationFilter("Unread") }
            ) {
                Text(
                    text = "Unread ",
                    color = if (activeFilter == "Unread") Color(0xFF1E6091) else Color.Gray,
                    fontWeight = if (activeFilter == "Unread") FontWeight.Bold else FontWeight.Normal
                )
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    val unreadCount = allNotifications.count { it.isUnread }
                    Text(text = unreadCount.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            FilterTab(
                text = "Announcements",
                isSelected = activeFilter == "Announcements",
                onClick = { moduleViewModel.setNotificationFilter("Announcements") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredNotifications) { alert ->
                NotificationRow(alert)
            }
        }
    }
}

@Composable
fun FilterTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        color = if (isSelected) Color(0xFF1E6091) else Color.Gray,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun NotificationRow(alert: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F9FA))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(alert.bulletColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = alert.icon, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = alert.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(text = alert.description, fontSize = 13.sp, color = Color.Gray)
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = alert.time, fontSize = 11.sp, color = Color.Gray)
            if (alert.isUnread) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E6091))
                )
            }
        }
    }
}
