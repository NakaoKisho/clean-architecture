package com.vegcale.architecture.data

import com.vegcale.architecture.data.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setIsNotificationOn(isOn: Boolean)
    suspend fun addPlaceIndex(index: Int)
    suspend fun clearPlaceIndexes()
    suspend fun deletePlaceIndex(index: Int)
    suspend fun addPlaceIndex(indexes: List<Int>)
    suspend fun setMinIntensityLevelIndex(index: Int)
}