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
    override suspend fun addPlace(place: String) {
        empPreferencesDataSource.addPlace(place)
    }
    override suspend fun clearPlaces() {
        empPreferencesDataSource.clearPlaces()
    }
    override suspend fun deletePlace(place: String) {
        empPreferencesDataSource.deletePlace(place)
    }
    override suspend fun addPlaces(places: List<String>) {
        empPreferencesDataSource.addPlaces(places)
    }

    // Min intensity level
    override suspend fun setMinIntensityLevelIndex(index: Int) {
        empPreferencesDataSource.setMinIntensityLevelIndex(index)
    }

    // Latest earthquake
    override val latestEarthquakeDatetime = empPreferencesDataSource.latestEarthquakeDatetime
    override val latestEarthquakeLatitude = empPreferencesDataSource.latestEarthquakeLatitude
    override val latestEarthquakeLongitude = empPreferencesDataSource.latestEarthquakeLongitude
    override val latestEarthquakeMagnitude = empPreferencesDataSource.latestEarthquakeMagnitude
    override suspend fun setLatestEarthquakeDatetime(datetime: String) {
        val byteStringText = ByteString.copyFromUtf8(datetime)
        empPreferencesDataSource.setLatestEarthquakeDatetime(byteStringText)
    }
    override suspend fun setLatestEarthquakeLatitude(latitude: Double) {
        empPreferencesDataSource.setLatestEarthquakeLatitude(latitude)
    }
    override suspend fun setLatestEarthquakeLongitude(longitude: Double) {
        empPreferencesDataSource.setLatestEarthquakeLongitude(longitude)
    }
    override suspend fun setLatestEarthquakeMagnitude(magnitude: Double) {
        empPreferencesDataSource.setLatestEarthquakeMagnitude(magnitude)
    }
}