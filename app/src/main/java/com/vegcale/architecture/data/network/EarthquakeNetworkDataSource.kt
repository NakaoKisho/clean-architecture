package com.vegcale.architecture.data.network

import com.vegcale.architecture.data.model.P2pquakeInfo
import com.vegcale.architecture.data.model.UsgsEarthquakeInfo

/**
 * このアプリのバックエンドに対するネットワークコールを定義するインターフェース
 */
interface UsgsEarthquakeApi {
    suspend fun getInfo(format:String, limit: Int, order: String): UsgsEarthquakeInfo
}

interface P2pquakeApi {
    suspend fun getInfo(limit: Int = 10, offset: Int = 0): List<P2pquakeInfo>
}