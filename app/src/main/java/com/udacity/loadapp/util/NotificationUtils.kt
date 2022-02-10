package com.udacity.loadapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.udacity.R

private const val NOTIFICATION_ID = 0
fun NotificationManager.sendNotification(channelId: String, message: Message, context: Context) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(message.title)
        .setContentText(message.content)

    notify(NOTIFICATION_ID, builder.build())
}

@RequiresApi(Build.VERSION_CODES.O)
fun createChannel(channel: Channel, context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    NotificationChannel(
        channel.id,
        channel.name,
        channel.importance
    ).apply {
        enableVibration(true)
        enableLights(true)
        setShowBadge(true)
        lightColor = context.getColor(R.color.colorAccent)
        description = channel.description
        lockscreenVisibility = channel.visibility
        notificationManager.createNotificationChannel(this)
    }

}