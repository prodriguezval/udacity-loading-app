package com.udacity.loadapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.loadapp.view.DetailActivity

private const val NOTIFICATION_ID = 0
fun NotificationManager.sendNotification(channelId: String, message: Message, context: Context) {
    val detailsIntent = createDetailsIntent(message, context)
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(message.title)
        .setContentText(message.content)
        .addAction(
            NotificationCompat.Action(
                0,
                context.getString(R.string.title_activity_detail),
                detailsIntent
            )
        )

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

fun clearNotification(context: Context, notificationId: Int) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.cancel(notificationId)
}

private fun createDetailsIntent(message: Message, context: Context): PendingIntent? {
    val notifyIntent = Intent(context, DetailActivity::class.java)
    notifyIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    notifyIntent.putExtras(
        DetailActivity.withExtras(
            NOTIFICATION_ID,
            message.content,
            message.title
        )
    )

    return PendingIntent.getActivity(
        context,
        1000,
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}