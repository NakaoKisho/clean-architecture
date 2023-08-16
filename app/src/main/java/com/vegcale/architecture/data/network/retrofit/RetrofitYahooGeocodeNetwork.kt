package com.vegcale.architecture.data.network.retrofit

import com.vegcale.architecture.data.model.YahooGeocodeInfo
import com.vegcale.architecture.data.network.YahooGeocodeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Named

enum class YahooGeocodeOutput {
    XML,
    JSON
}
interface RetrofitYahooGeocodeNetworkApi {
    @GET
    suspend fun getInfo(
        @Query("appid") appId: String,
        @Query("query") query: String,
        @Query("recursive") recursive: Boolean,
        @Query("results") result: Byte,
        @Query("output") output: YahooGeocodeOutput,
    ): YahooGeocodeInfo
}

class RetrofitYahooGeocodeNetwork @Inject constructor(
    @Named("yahooGeocodeUrl") baseUrl: String
): YahooGeocodeApi {
    private val retrofitYahooGeocodeNetworkApi =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitYahooGeocodeNetworkApi::class.java)

    override suspend fun getInfo(
        appId: String,
        query: String,
        recursive: Boolean,
        result: Byte,
        output: YahooGeocodeOutput
    ): YahooGeocodeInfo {
        return retrofitYahooGeocodeNetworkApi
            .getInfo(
                appId,
                query,
                recursive,
                result,
                output
            )
    }
}