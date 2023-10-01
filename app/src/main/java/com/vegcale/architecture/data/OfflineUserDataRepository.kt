package com.vegcale.architecture.data

import com.google.protobuf.ByteString
import com.vegcale.architecture.datastore.EmpPreferencesDataSource
import javax.inject.Inject

class OfflineUserDataRepository @Inject constructor(
    private val empPreferencesDataSource: EmpPreferencesDataSource,
): UserDataRepository {
    override val userData = empPreferencesDataSource.userData

    // Notification
    override suspend fun setIsNotificationOn(isOn: Boolean) {
        empPreferencesDataSource.setIsNotificationOn(isOn)
    }

    // Places
    override suspend fun addPlaceIndex(index: Int) {
        empPreferencesDataSource.addPlaceIndex(index)
    }
    override suspend fun clearPlaceIndexes() {
        empPreferencesDataSource.clearPlaceIndexes()
    }
    override suspend fun deletePlaceIndex(index: Int) {
        empPreferencesDataSource.deletePlaceIndex(index)
    }
    override suspend fun addPlaceIndex(indexes: List<Int>) {
        empPreferencesDataSource.addPlaceIndex(indexes)
    }

    // Min intensity level
    override suspend fun setMinIntensityLevelIndex(index: Int) {
        empPreferencesDataSource.setMinIntensityLevelIndex(index)
    }

    // Latest earthquake
    val latestEarthquakeDatetime = empPreferencesDataSource.latestEarthquakeDatetime
    suspend fun setLatestEarthquakeDatetime(datetime: String) {
        val byteStringText = ByteString.copyFromUtf8(datetime)
        empPreferencesDataSource.setLatestEarthquakeDatetime(byteStringText)
    }
    val latestEarthquakeLatitude = empPreferencesDataSource.latestEarthquakeLatitude
    suspend fun setLatestEarthquakeLatitude(latitude: Double) {
        empPreferencesDataSource.setLatestEarthquakeLatitude(latitude)
    }
    val latestEarthquakeLongitude = empPreferencesDataSource.latestEarthquakeLongitude
    suspend fun setLatestEarthquakeLongitude(longitude: Double) {
        empPreferencesDataSource.setLatestEarthquakeLongitude(longitude)
    }
    val latestEarthquakeMagnitude = empPreferencesDataSource.latestEarthquakeMagnitude
    suspend fun setLatestEarthquakeMagnitude(magnitude: Double) {
        empPreferencesDataSource.setLatestEarthquakeMagnitude(magnitude)
    }
}