package com.vegcale.architecture.usecase

import android.util.Log
import com.vegcale.architecture.BuildConfig
import com.vegcale.architecture.data.P2pquakeRepository
import com.vegcale.architecture.data.Result
import com.vegcale.architecture.data.UsgsEarthquakeRepository
import com.vegcale.architecture.data.YahooGeocodeRepository
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.Points
import com.vegcale.architecture.data.model.getDatetime
import com.vegcale.architecture.data.model.getDepth
import com.vegcale.architecture.data.model.getLatitude
import com.vegcale.architecture.data.model.getLongitude
import com.vegcale.architecture.data.succeeded
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "GetLatestEarthquakeInfoUseCase"
class GetLatestEarthquakeInfoUseCase @Inject constructor(
    private val usgsEarthquakeRepository: UsgsEarthquakeRepository,
    private val p2pquakeRepository: P2pquakeRepository,
    private val yahooGeocodeRepository: YahooGeocodeRepository,
) {
    operator fun invoke(): Flow<List<EarthquakeInfo>> {
        Log.i(TAG, "GetLatestEarthquakeInfoUseCase.invoke() starts")
        return flow {
            while (true) {
                val usgsResult = usgsEarthquakeRepository.getInfo("geojson", 10, "time")
                var usgsList: List<EarthquakeInfo> = emptyList()
                if (usgsResult.succeeded) {
                    usgsList = (usgsResult as Result.Success).data
                        .features
                        .map { feature ->
                            EarthquakeInfo(
                                feature.properties.getDatetime(),
                                feature.properties.place ?: "",
                                feature.geometry.coordinates[1],
                                feature.geometry.coordinates[0],
                                feature.properties.mag,
                                feature.geometry.getDepth(),
                                listOf(Points("",0.0,0.0, 10))
                            )
                        }
                }

                val p2pResult = p2pquakeRepository.getInfo(10, 0)
                var p2pList: List<EarthquakeInfo> = emptyList()
                val yahooApiAppId = BuildConfig.YAHOO_CLIENT_ID
                if (p2pResult.succeeded) {
                    p2pList = (p2pResult as Result.Success).data
                        .map { p2pquakeInfo ->
                            EarthquakeInfo(
                                p2pquakeInfo.earthquake.getDatetime(),
                                p2pquakeInfo.earthquake.hypocenter.name,
                                p2pquakeInfo.earthquake.hypocenter.latitude,
                                p2pquakeInfo.earthquake.hypocenter.longitude,
                                p2pquakeInfo.earthquake.hypocenter.magnitude,
                                p2pquakeInfo.earthquake.hypocenter.depth,
                                p2pquakeInfo.points.map {
                                    val yahooGeocodeResult = yahooGeocodeRepository.getInfo(
                                        appId = yahooApiAppId,
                                        query = it.addr
                                    )

                                    if (
                                        yahooGeocodeResult.succeeded &&
                                        (yahooGeocodeResult as Result.Success).data.resultInfo != null &&
                                        yahooGeocodeResult.data.features != null
                                    ) {
                                        val geometry = yahooGeocodeResult
                                            .data
                                            .features[0]
                                            .geometry
                                        Points(
                                            place = it.addr,
                                            latitude = geometry.getLatitude(),
                                            longitude = geometry.getLongitude(),
                                            scale = it.scale
                                        )
                                    } else {
                                        Points(
                                            place = null,
                                            latitude = null,
                                            longitude = null,
                                            scale = null
                                        )
                                    }
                                }
                            )
                        }
                }
                emit(usgsList + p2pList)
                delay(15000L)
            }
        }
    }
}