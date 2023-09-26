package com.vegcale.architecture.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vegcale.architecture.R
import com.vegcale.architecture.ui.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val EARTHQUAKE_NOTIFICATION_REQUEST_CODE = 0
private const val EARTHQUAKE_NOTIFICATION_CHANNEL_ID = ""

@Singleton
class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context
): Notifier {
    override fun postNewsNotifications() {
        with(context) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            val pendingIntent = this.earthquakePendingIntent()
            val earthquakeNotification = createNotification {
                setSmallIcon(R.drawable.baseline_access_time_24)
                .setLargeIcon(Icon.createWithResource(context, R.drawable.baseline_access_time_24))
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
            }
            NotificationManagerCompat.from(this).notify(1, earthquakeNotification)
        }
    }
}

private fun Context.createNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    createNotificationChannel()

    return NotificationCompat.Builder(
        this,
        EARTHQUAKE_NOTIFICATION_CHANNEL_ID
    )
        .apply(block)
        .setAutoCancel(true)
        .build()
}

private fun Context.createNotificationChannel() {
    val name = getString(R.string.earthquake_info)
    val descriptionText = getString(R.string.earthquake_info_detail)
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(
        EARTHQUAKE_NOTIFICATION_CHANNEL_ID,
        name,
        importance
    ).apply {
        description = descriptionText
        enableVibration(true)
        vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
    }
    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

private fun Context.earthquakePendingIntent(): PendingIntent {
    val intent = Intent(
        this,
        MainActivity::class.java
    ).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    return PendingIntent.getActivity(
        this,
        EARTHQUAKE_NOTIFICATION_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}