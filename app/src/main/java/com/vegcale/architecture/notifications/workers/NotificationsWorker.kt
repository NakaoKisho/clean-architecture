package com.vegcale.architecture.notifications.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.vegcale.architecture.data.OfflineUserDataRepository
import com.vegcale.architecture.data.model.SeismicIntensity2
import com.vegcale.architecture.network.Dispatcher
import com.vegcale.architecture.network.EmpDispatchers
import com.vegcale.architecture.notifications.Notifier
import com.vegcale.architecture.usecase.GetEarthquakeSummaryUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class NotificationsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(EmpDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getEarthquakeSummaryUseCase: GetEarthquakeSummaryUseCase,
    private val systemTrayNotifier: Notifier,
    private val offlineUserDataRepository: OfflineUserDataRepository
): CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        while (true) {
            val userData = offlineUserDataRepository.userData.first()
            val minIntensityLevel = convertToSeismicIntensity(userData.minIntensityLevelIndex)
            if (userData.isNotificationOn) {
                withContext(ioDispatcher) {
                    getEarthquakeSummaryUseCase(userData.places, minIntensityLevel).collectLatest {
                        if (it.isEmpty()) return@collectLatest
                        offlineUserDataRepository.setLatestEarthquakeDatetime(it.first().datetime)
                        offlineUserDataRepository.setLatestEarthquakeLatitude(it.first().latitude)
                        offlineUserDataRepository.setLatestEarthquakeLongitude(it.first().longitude)
                        offlineUserDataRepository.setLatestEarthquakeMagnitude(it.first().magnitude)

                        val summary = it.first()
                        val contentTitle = String.format("%sで地震が発生しました", summary.place)
                        val contentText = String.format(
                            "場所:%s\n時間:%s\nマグニチュード:%s",
                            summary.place,
                            summary.datetime,
                            summary.magnitude
                        )
                        systemTrayNotifier.postNewsNotifications(contentTitle, contentText)
                    }
                }
            }
            delay(30000)
        }
    }

    private fun convertToSeismicIntensity(intensityLevelIndex: Int) =
        when (intensityLevelIndex) {
            0 -> SeismicIntensity2.IntensityOfOne
            1 -> SeismicIntensity2.IntensityOfTwo
            2 -> SeismicIntensity2.IntensityOfThree
            3 -> SeismicIntensity2.IntensityOfFour
            4 -> SeismicIntensity2.IntensityOfLowerFive
            5 -> SeismicIntensity2.IntensityOfUpperFive
            6 -> SeismicIntensity2.IntensityOfLowerSix
            7 -> SeismicIntensity2.IntensityOfUpperSix
            8 -> SeismicIntensity2.IntensityOfSeven
            else -> throw Error("intensityLevel should be from 0 to 8")
        }

    companion object {
        private const val WorkTag = "Notification Worker"
        fun startUpNotificationWork() = OneTimeWorkRequestBuilder<NotificationsWorker>()
            .addTag(WorkTag)
            .build()
    }
}