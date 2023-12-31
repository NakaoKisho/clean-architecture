package com.vegcale.architecture.di

import com.vegcale.architecture.BuildConfig
import com.vegcale.architecture.data.network.P2pquakeApi
import com.vegcale.architecture.data.network.UsgsEarthquakeApi
import com.vegcale.architecture.data.network.YahooGeocodeApi
import com.vegcale.architecture.data.network.retrofit.RetrofitP2pquakeNetwork
import com.vegcale.architecture.data.network.retrofit.RetrofitP2pquakeNetworkApi
import com.vegcale.architecture.data.network.retrofit.RetrofitUsgsEarthquakeNetwork
import com.vegcale.architecture.data.network.retrofit.RetrofitUsgsEarthquakeNetworkApi
import com.vegcale.architecture.data.network.retrofit.RetrofitYahooGeocodeNetwork
import com.vegcale.architecture.data.network.retrofit.RetrofitYahooGeocodeNetworkApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
abstract class EarthquakeApiModule {
    @Binds
    abstract fun bindUsgsEarthquakeApi(
        retrofitUsgsEarthquakeNetwork: RetrofitUsgsEarthquakeNetwork
    ): UsgsEarthquakeApi

    @Binds
    abstract fun bindP2pquakeApi(
        retrofitP2pEarthquakeNetwork: RetrofitP2pquakeNetwork
    ): P2pquakeApi

    @Binds
    abstract fun bindYahooGeocodeApi(
        retrofitYahooGeocodeNetwork: RetrofitYahooGeocodeNetwork
    ): YahooGeocodeApi
}

@Module
@InstallIn(SingletonComponent::class)
object EarthquakeNetworkModule {
    private const val UsgsBaseUrl = BuildConfig.USGS_EARTHQUAKE_API_URL
    @Provides
    @Named("usgsUrl")
    fun provideUsgsString() = UsgsBaseUrl
    @Provides
    fun provideUsgsEarthquakeRetrofit(): RetrofitUsgsEarthquakeNetworkApi {
        return Retrofit
            .Builder()
            .baseUrl(UsgsBaseUrl)
            .build()
            .create(RetrofitUsgsEarthquakeNetworkApi::class.java)
    }

    private const val P2pBaseUrl = BuildConfig.P2P_QUAKE_API_URL
    @Provides
    @Named("p2pUrl")
    fun provideP2pString() = P2pBaseUrl
    @Provides
    fun provideP2pquakeRetrofit(): RetrofitP2pquakeNetworkApi {
        return Retrofit
            .Builder()
            .baseUrl(P2pBaseUrl)
            .build()
            .create(RetrofitP2pquakeNetworkApi::class.java)
    }

    private const val YahooGeocodeBaseUrl = BuildConfig.YAHOO_GEOCODE_API_URL
    @Provides
    @Named("yahooGeocodeUrl")
    fun provideYahooGeocodeString() = YahooGeocodeBaseUrl
    @Provides
    fun provideYahooGeocodeRetrofit(): RetrofitYahooGeocodeNetworkApi {
        return Retrofit
            .Builder()
            .baseUrl(YahooGeocodeBaseUrl)
            .build()
            .create(RetrofitYahooGeocodeNetworkApi::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnconfinedDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object CoroutineDispatcherModule {
    @DefaultDispatcher
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }
}