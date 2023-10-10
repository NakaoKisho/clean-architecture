package com.vegcale.architecture.data.network.retrofit.fake

import com.vegcale.architecture.data.model.YahooGeocodeInfo
import com.vegcale.architecture.data.network.YahooGeocodeApi

class FakeRetrofitYahooGeocodeNetwork(
    private val yahooGeocodeInfo: YahooGeocodeInfo =
        YahooGeocodeInfo(
            resultInfo = null,
            features = null
        )
): YahooGeocodeApi {
    override suspend fun getInfo(
        appId: String,
        query: String,
        recursive: Boolean,
        result: Byte,
        output: String
    ): YahooGeocodeInfo {
        return yahooGeocodeInfo
    }
}