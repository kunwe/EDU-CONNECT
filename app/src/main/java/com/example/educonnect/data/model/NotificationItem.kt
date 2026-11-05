package com.example.educonnect.data.model

import androidx.compose.ui.graphics.Color

data class NotificationItem(
    val icon: String,
    val title: String,
    val description: String,
    val time: String,
    val isUnread: Boolean,
    val bulletColor: Color
)
