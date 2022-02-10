package com.udacity.loadapp.util

import android.app.NotificationManager
import androidx.core.app.NotificationCompat

data class Channel(
    val id: String,
    val name: String,
    val description: String,
    val priority: Int = NotificationManager.IMPORTANCE_HIGH,
    val visibility: Int = NotificationCompat.PRIORITY_HIGH,
    val importance: Int = NotificationManager.IMPORTANCE_HIGH
)
