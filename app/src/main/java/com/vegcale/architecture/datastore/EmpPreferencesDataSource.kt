package com.vegcale.architecture.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
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
}