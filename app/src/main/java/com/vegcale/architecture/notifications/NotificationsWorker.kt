package com.vegcale.architecture.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class NotificationsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val systemTrayNotifier: Notifier
): CoroutineWorker(appContext, workerParams) {
    object Constants {
        const val TAG = "Notification Worker"
        const val NAME = "Notification Worker"
    }
    override suspend fun doWork(): Result {
        while (true) {
            systemTrayNotifier.postNewsNotifications()
            delay(30000)
        }
    }
}