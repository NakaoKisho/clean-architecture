package com.vegcale.architecture.data.network

import com.vegcale.architecture.data.model.P2pquakeInfo
import com.vegcale.architecture.data.model.UsgsEarthquakeInfo
import com.vegcale.architecture.data.model.YahooGeocodeInfo
import com.vegcale.architecture.data.network.retrofit.YahooGeocodeOutput

/**
 * このアプリのバックエンドに対するネットワークコールを定義するインターフェース
 */
interface UsgsEarthquakeApi {
    suspend fun getInfo(format:String, limit: Int, order: String): UsgsEarthquakeInfo
}

interface P2pquakeApi {
    suspend fun getInfo(limit: Int = 10, offset: Int = 0): List<P2pquakeInfo>
}

interface  YahooGeocodeApi {
    suspend fun getInfo(
        appId: String,
        query: String,
        recursive: Boolean,
        result: Byte,
        output: YahooGeocodeOutput
    ): YahooGeocodeInfo
}