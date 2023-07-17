package com.vegcale.architecture.data

import androidx.annotation.IntRange
import com.vegcale.architecture.data.model.P2pquakeInfo
import com.vegcale.architecture.data.network.P2pquakeApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class P2pquakeRepository @Inject constructor(
    private val network: P2pquakeApi
) {
    fun getInfo(
        @IntRange(from = 1, to = 100) limit: Int,
        @IntRange(from = 0, to = 100) offset: Int
    ): Flow<List<P2pquakeInfo>> {
        val intervalMillisecond = 15000L

        return flow {
            while (true) {
                val latestEarthquake = network.getInfo(limit, offset)
                emit(latestEarthquake)

                delay(intervalMillisecond)
            }
        }
    }
}