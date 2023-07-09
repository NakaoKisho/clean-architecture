package com.vegcale.architecture.data.network

import com.vegcale.architecture.data.model.EarthquakeInfo

/**
 * このアプリのバックエンドに対するネットワークコールを定義するインターフェース
 */
interface EarthquakeApi {
    suspend fun getInfo(format:String, limit: Int, order: String): EarthquakeInfo
}