package com.vegcale.architecture.data.network

import androidx.annotation.IntRange
import com.vegcale.architecture.data.model.EarthquakeInfo

/**
 * このアプリのバックエンドに対するネットワークコールを定義するインターフェース
 */
interface EarthquakeApi {
    suspend fun getInfo(format:String, limit: Int, order: String): EarthquakeInfo
}

enum class P2pquakeCode {
    ALL,        // 551, 552
    EARTHQUAKE, // 551
    TSUNAMI     // 552
}

interface P2pquakeApi {
    suspend fun getHistory(
        codes: P2pquakeCode,
        @IntRange(from = 1, to = 100) limit: Int,
        @IntRange(from = 0, to = 100) offset: Int
    ): Unit

    suspend fun getInfo(id: Int): Unit
}