package com.vegcale.architecture.data.network

import com.vegcale.architecture.data.model.YahooGeocodeInfo
import com.vegcale.architecture.data.network.retrofit.YahooGeocodeOutput


interface  YahooGeocodeApi {
    suspend fun getInfo(
        appId: String,
        query: String,
        recursive: Boolean,
        result: Byte,
        output: YahooGeocodeOutput
    ): YahooGeocodeInfo
}