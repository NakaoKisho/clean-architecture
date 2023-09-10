package com.vegcale.architecture.data

import androidx.annotation.IntRange
import com.vegcale.architecture.data.model.YahooGeocodeInfo
import com.vegcale.architecture.data.network.YahooGeocodeApi
import com.vegcale.architecture.data.network.retrofit.YahooGeocodeOutputFormat
import javax.inject.Inject

class YahooGeocodeRepository2 @Inject constructor(
    private val network: YahooGeocodeApi
) {
    suspend fun getInfo(
        appId: String,
        query: String,
        recursive: Boolean = true,
        @IntRange(from = 1, to = 100) result: Byte = 1,
        output: YahooGeocodeOutputFormat = YahooGeocodeOutputFormat.JSON
    ): YahooGeocodeInfo {
        val outputFormat = if (output == YahooGeocodeOutputFormat.JSON) "json" else "xml"

        return network.getInfo(appId, query, recursive, result, outputFormat)
    }
}