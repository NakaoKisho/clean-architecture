package com.vegcale.architecture.data

import androidx.annotation.IntRange
import com.vegcale.architecture.data.model.P2pquakeInfo
import com.vegcale.architecture.data.network.P2pquakeApi
import javax.inject.Inject

class P2pquakeRepository @Inject constructor(
    private val network: P2pquakeApi
) {
    suspend fun getInfo(
        @IntRange(from = 1, to = 100) limit: Int,
        @IntRange(from = 0, to = 100) offset: Int
    ): List<P2pquakeInfo> {
        return network.getInfo(limit, offset)
    }
}