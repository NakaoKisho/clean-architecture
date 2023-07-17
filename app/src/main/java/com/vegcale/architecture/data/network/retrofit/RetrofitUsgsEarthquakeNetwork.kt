package com.vegcale.architecture.data.network.retrofit

import com.vegcale.architecture.BuildConfig
import com.vegcale.architecture.data.model.UsgsEarthquakeInfo
import com.vegcale.architecture.data.network.UsgsEarthquakeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

/**
 * Retrofitのcreate()の引数になるインターフェース
 * Retrofitはインターフェースで定義されたAPIエンドポイントの実装を作成する
 */
interface RetrofitUsgsEarthquakeNetworkApi {
    @GET("query")
    suspend fun getInfo(
        @Query("format") format: String,
        @Query("limit") limit: Int,
        @Query("orderby") orderBy: String,
    ): UsgsEarthquakeInfo
}

private const val BaseUrl = BuildConfig.USGS_EARTHQUAKE_API_URL

class RetrofitUsgsEarthquakeNetwork @Inject constructor(): UsgsEarthquakeApi {
    private val retrofitUsgsEarthquakeNetworkApi =
        Retrofit
            .Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitUsgsEarthquakeNetworkApi::class.java)

    /**
     * RetrofitEarthquakeNetworkクラスがEarthquakeNetworkDataSourceを継承しているため、
     * getInfo(limit: String): List<UsgsEarthquakeInfo>をオーバーライドする必要がある。
     */
    override suspend fun getInfo(format:String, limit: Int, order: String): UsgsEarthquakeInfo {
        return retrofitUsgsEarthquakeNetworkApi.getInfo(format, limit, order)
    }
}