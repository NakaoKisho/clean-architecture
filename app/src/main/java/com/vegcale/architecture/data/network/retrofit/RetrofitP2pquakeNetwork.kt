package com.vegcale.architecture.data.network.retrofit

import com.vegcale.architecture.data.model.P2pquakeInfo
import com.vegcale.architecture.data.network.P2pquakeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Named

interface RetrofitP2pquakeNetworkApi {
    @GET("?codes=551")
    suspend fun getInfo(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): List<P2pquakeInfo>
}

class RetrofitP2pquakeNetwork @Inject constructor(@Named("p2pUrl") baseUrl: String): P2pquakeApi {
    private val retrofitP2pquakeNetworkApi =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitP2pquakeNetworkApi::class.java)

    override suspend fun getInfo(limit: Int, offset: Int): List<P2pquakeInfo> {
        return retrofitP2pquakeNetworkApi.getInfo(limit, offset)
    }
}