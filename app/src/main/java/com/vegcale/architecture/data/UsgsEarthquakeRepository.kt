package com.vegcale.architecture.data

import com.vegcale.architecture.data.model.UsgsEarthquakeInfo
import com.vegcale.architecture.data.network.UsgsEarthquakeApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsgsEarthquakeRepository @Inject constructor(
    private val network: UsgsEarthquakeApi
) {
    fun getInfo(format:String, limit: Int, order: String): Flow<UsgsEarthquakeInfo> {
        val intervalMillisecond = 15000L

        return flow {
            while (true) {
                val latestEarthquake = network.getInfo(format, limit, order)
                emit(latestEarthquake)

                delay(intervalMillisecond)
            }
        }
    }
}