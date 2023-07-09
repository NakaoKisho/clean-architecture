package com.vegcale.architecture.data.network.retrofit

import com.vegcale.architecture.BuildConfig
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.network.EarthquakeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

/**
 * Retrofitのcreate()の引数になるインターフェース
 * Retrofitはインターフェースで定義されたAPIエンドポイントの実装を作成する
 */
interface RetrofitEarthquakeNetworkApi {
    @GET("query")
    suspend fun getInfo(
        @Query("format") format: String,
        @Query("limit") limit: Int,
        @Query("orderby") orderBy: String,
    ): EarthquakeInfo
}

private const val BaseUrl = BuildConfig.EARTHQUAKE_API_URL

//private data class NetworkResponse<T>(
//    val data: T,
//)

class RetrofitEarthquakeNetwork @Inject constructor(): EarthquakeApi {
    private val retrofitEarthquakeNetworkApi =
        Retrofit
            .Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitEarthquakeNetworkApi::class.java)

    /**
     * RetrofitEarthquakeNetworkクラスがEarthquakeNetworkDataSourceを継承しているため、
     * getInfo(limit: String): List<EarthquakeInfo>をオーバーライドする必要がある。
     */
    override suspend fun getInfo(format:String, limit: Int, order: String): EarthquakeInfo {
        return retrofitEarthquakeNetworkApi.getInfo(format, limit, order)
    }
}