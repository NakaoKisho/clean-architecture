package com.vegcale.architecture.data

import com.vegcale.architecture.data.model.UsgsEarthquakeInfo
import com.vegcale.architecture.data.network.UsgsEarthquakeApi
import javax.inject.Inject

class UsgsEarthquakeRepository @Inject constructor(
    private val network: UsgsEarthquakeApi
) {
    suspend fun getInfo(format:String, limit: Int, order: String): UsgsEarthquakeInfo {
        return network.getInfo(format, limit, order)
    }
}