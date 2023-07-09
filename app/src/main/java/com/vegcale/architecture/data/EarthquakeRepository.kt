package com.vegcale.architecture.data

import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.network.EarthquakeApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EarthquakeRepository @Inject constructor(
    private val network: EarthquakeApi
) {
    fun getInfo(format:String, limit: Int, order: String): Flow<EarthquakeInfo> {
        return flow {
            while (true) {
                val latestEarthquake = network.getInfo(format, limit, order)
                emit(latestEarthquake)

                delay(600000)
            }
        }
    }
}