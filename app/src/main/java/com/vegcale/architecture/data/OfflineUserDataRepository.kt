package com.vegcale.architecture.data

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
}