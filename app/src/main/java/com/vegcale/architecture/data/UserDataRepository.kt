package com.vegcale.architecture.data

import com.vegcale.architecture.data.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    // Notification
    suspend fun setIsNotificationOn(isOn: Boolean)
    // Places
    suspend fun addPlace(place: String)
    suspend fun clearPlaces()
    suspend fun deletePlace(place: String)
    suspend fun addPlaces(places: List<String>)
    // Min intensity level
    suspend fun setMinIntensityLevelIndex(index: Int)
    // Latest earthquake
    val latestEarthquakeDatetime: Flow<String>
    val latestEarthquakeLatitude: Flow<Double>
    val latestEarthquakeLongitude: Flow<Double>
    val latestEarthquakeMagnitude: Flow<Double>
    suspend fun setLatestEarthquakeDatetime(datetime: String)
    suspend fun setLatestEarthquakeLatitude(latitude: Double)
    suspend fun setLatestEarthquakeLongitude(longitude: Double)
    suspend fun setLatestEarthquakeMagnitude(magnitude: Double)
}