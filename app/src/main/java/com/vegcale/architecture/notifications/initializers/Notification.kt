package com.vegcale.architecture.notifications.initializers

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.vegcale.architecture.notifications.workers.NotificationsWorker

object Notification {
    fun initialize(context: Context) {
        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                NotificationWorkName,
                ExistingWorkPolicy.KEEP,
                NotificationsWorker.startUpNotificationWork()
            )
    }
}

internal const val NotificationWorkName = "Notification Work"