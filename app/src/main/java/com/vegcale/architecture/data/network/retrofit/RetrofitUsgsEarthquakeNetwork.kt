package com.vegcale.architecture.data.network.retrofit

import com.vegcale.architecture.data.model.UsgsEarthquakeInfo
import com.vegcale.architecture.data.network.UsgsEarthquakeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Named

interface RetrofitUsgsEarthquakeNetworkApi {
    @GET("query")
    suspend fun getInfo(
        @Query("format") format: String,
        @Query("limit") limit: Int,
        @Query("orderby") orderBy: String,
    ): UsgsEarthquakeInfo
}

class RetrofitUsgsEarthquakeNetwork @Inject constructor(
    @Named("usgsUrl") baseUrl: String
): UsgsEarthquakeApi {
    private val retrofitUsgsEarthquakeNetworkApi =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitUsgsEarthquakeNetworkApi::class.java)

    override suspend fun getInfo(format:String, limit: Int, order: String): UsgsEarthquakeInfo {
        return retrofitUsgsEarthquakeNetworkApi.getInfo(format, limit, order)
    }
}