package com.vegcale.architecture.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.google.protobuf.ByteString
import com.vegcale.architecture.data.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmpPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map {
            UserData(
                isNotificationOn = it.isNotificationOn,
                places = it.placesList,
                minIntensityLevelIndex = it.minIntensityLevelIndex
            )
        }

    suspend fun setIsNotificationOn(isOn: Boolean) {
        try {
            userPreferences.updateData {
                it.toBuilder().setIsNotificationOn(isOn).build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to setIsNotificationOn", ioException)
        }
    }

    // Places
    suspend fun addPlace(place: String) {
        try {
            userPreferences.updateData {
                it.toBuilder().addPlaces(place).build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to addPlace", ioException)
        }
    }
    suspend fun clearPlaces() {
        try {
            userPreferences.updateData {
                it.toBuilder().clearPlaces().build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to clearPlaces", ioException)
        }
    }
    suspend fun deletePlace(place: String) {
        try {
            userPreferences.updateData {
                val newList = it.toBuilder().placesList.toMutableList()
                newList.remove(place)
                it.toBuilder()
                    .clearPlaces()
                    .addAllPlaces(newList)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to deletePlace", ioException)
        }
    }
    suspend fun addPlaces(places: List<String>) {
        try {
            userPreferences.updateData {
                it.toBuilder()
                    .clearPlaces()
                    .addAllPlaces(places)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to addPlaces", ioException)
        }
    }

    // Min intensity level
    suspend fun setMinIntensityLevelIndex(index: Int) {
        try {
            userPreferences.updateData {
                it.toBuilder().setMinIntensityLevelIndex(index).build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to setMinIntensityLevelIndex", ioException)
        }
    }

    // Latest earthquake
    val latestEarthquakeDatetime = userPreferences.data
        .map {
            it.latestEarthquakeDatetime
        }
    suspend fun setLatestEarthquakeDatetime(datetime: ByteString) {
        try {
            userPreferences.updateData {
                it.toBuilder()
                    .setLatestEarthquakeDatetimeBytes(datetime)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to setLatestEarthquakeDatetime", ioException)
        }
    }
    val latestEarthquakeLatitude = userPreferences.data
        .map {
            it.latestEarthquakeLatitude
        }
    suspend fun setLatestEarthquakeLatitude(latitude: Double) {
        try {
            userPreferences.updateData {
                it.toBuilder()
                    .setLatestEarthquakeLatitude(latitude)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to setLatestEarthquakeLatitude", ioException)
        }
    }
    val latestEarthquakeLongitude = userPreferences.data
        .map {
            it.latestEarthquakeLongitude
        }
    suspend fun setLatestEarthquakeLongitude(longtitude: Double) {
        try {
            userPreferences.updateData {
                it.toBuilder()
                    .setLatestEarthquakeLongitude(longtitude)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to setLatestEarthquakeLongitude", ioException)
        }
    }
    val latestEarthquakeMagnitude = userPreferences.data
        .map {
            it.latestEarthquakeMagnitude
        }
    suspend fun setLatestEarthquakeMagnitude(magnitude: Double) {
        try {
            userPreferences.updateData {
                it.toBuilder()
                    .setLatestEarthquakeMagnitude(magnitude)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to setLatestEarthquakeMagnitude", ioException)
        }
    }
}