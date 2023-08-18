package com.vegcale.architecture.data

import androidx.annotation.IntRange
import com.vegcale.architecture.data.model.YahooGeocodeInfo
import com.vegcale.architecture.data.network.YahooGeocodeApi
import com.vegcale.architecture.data.network.retrofit.YahooGeocodeOutputFormat
import javax.inject.Inject

class YahooGeocodeRepository @Inject constructor(
    private val network: YahooGeocodeApi
) {
    suspend fun getInfo(
        appId: String,
        query: String,
        recursive: Boolean = true,
        @IntRange(from = 1, to = 100) result: Byte = 1,
        output: YahooGeocodeOutputFormat = YahooGeocodeOutputFormat.JSON
    ): Result<YahooGeocodeInfo> {
        val outputFormat = if (output == YahooGeocodeOutputFormat.JSON) "json" else "xml"

        return try {
            val data = network.getInfo(appId, query, recursive, result, outputFormat)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}