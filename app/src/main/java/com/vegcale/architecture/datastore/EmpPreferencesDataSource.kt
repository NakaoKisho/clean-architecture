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
                placeIndexes = it.placeIndexesList,
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
    suspend fun addPlaceIndex(index: Int) {
        try {
            userPreferences.updateData {
                it.toBuilder().addPlaceIndexes(index).build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to addPlaceIndex", ioException)
        }
    }
    suspend fun clearPlaceIndexes() {
        try {
            userPreferences.updateData {
                it.toBuilder().clearPlaceIndexes().build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to clearPlaceIndexes", ioException)
        }
    }
    suspend fun deletePlaceIndex(index: Int) {
        try {
            userPreferences.updateData {
                val newList = it.toBuilder().placeIndexesList.toMutableList()
                newList.remove(index)
                it.toBuilder()
                    .clearPlaceIndexes()
                    .addAllPlaceIndexes(newList)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to deletePlaceIndex", ioException)
        }
    }
    suspend fun addPlaceIndex(indexes: List<Int>) {
        try {
            userPreferences.updateData {
                with (it.toBuilder()) {
                    this.clearPlaceIndexes()
                    this.addAllPlaceIndexes(indexes)

                    return@with this
                }.build()
            }
        } catch (ioException: IOException) {
            Log.e("EmpPreferences", "Failed to addPlaceIndex", ioException)
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