package com.vegcale.architecture.data

import com.vegcale.architecture.data.model.UsgsEarthquakeInfo
import com.vegcale.architecture.data.network.UsgsEarthquakeApi
import javax.inject.Inject

class UsgsEarthquakeRepository @Inject constructor(
    private val network: UsgsEarthquakeApi
) {
    suspend fun getInfo(format:String, limit: Int, order: String): Result<UsgsEarthquakeInfo> {
        return try {
            val data = network.getInfo(format, limit, order)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}