package com.vegcale.architecture.di

import com.vegcale.architecture.data.network.EarthquakeApi
import com.vegcale.architecture.data.network.retrofit.RetrofitEarthquakeNetwork
import com.vegcale.architecture.data.network.retrofit.RetrofitEarthquakeNetworkApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
// インターフェースのBinds
abstract class EarthquakeApiModule {
    @Binds
    // 関数の戻り値の型で、その関数が提供するインターフェースのインスタンスを知らせます。 -> EarthquakeApi
    abstract fun bindEarthquakeApi(
        // 関数のパラメータで、提供する実装を知らせます。 -> RetrofitEarthquakeNetwork
        retrofitEarthquakeNetwork: RetrofitEarthquakeNetwork
    ): EarthquakeApi
}

@Module
@InstallIn(ViewModelComponent::class)
object EarthquakeNetworkModule {
    private const val BaseUrl = "https://earthquake.usgs.gov/fdsnws/event/1/"
    @Provides
    fun provideEarthquakeRetrofit(): RetrofitEarthquakeNetworkApi {
        return Retrofit
            .Builder()
            .baseUrl(BaseUrl)
            .build()
            .create(RetrofitEarthquakeNetworkApi::class.java)
    }
}