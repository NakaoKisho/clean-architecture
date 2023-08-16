package com.vegcale.architecture.data

import com.vegcale.architecture.data.model.YahooGeocodeInfo
import com.vegcale.architecture.data.network.YahooGeocodeApi
import com.vegcale.architecture.data.network.retrofit.YahooGeocodeOutput
import javax.inject.Inject

class YahooGeocodeRepository @Inject constructor(
    private val network: YahooGeocodeApi
) {
    suspend fun getInfo(
        appId: String,
        query: String,
        recursive: Boolean,
        result: Byte,
        output: YahooGeocodeOutput
    ): Result<YahooGeocodeInfo> {
        return try {
            val data = network.getInfo(appId, query, recursive, result, output)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}