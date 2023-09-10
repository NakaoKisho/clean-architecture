package com.vegcale.architecture.usecase

import com.vegcale.architecture.BuildConfig
import com.vegcale.architecture.data.P2pquakeRepository2
import com.vegcale.architecture.data.YahooGeocodeRepository2
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.Points
import com.vegcale.architecture.data.model.getDatetime
import com.vegcale.architecture.data.model.getLatitude
import com.vegcale.architecture.data.model.getLongitude
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLatestEarthquakeInfoUseCase2 @Inject constructor(
    private val p2pquakeRepository: P2pquakeRepository2,
    private val yahooGeocodeRepository: YahooGeocodeRepository2,
) {
    operator fun invoke(): Flow<EarthquakeInfo> {
        val yahooApiAppId = BuildConfig.YAHOO_CLIENT_ID

        return flow {
            p2pquakeRepository
                .getInfo(10, 0)
                .map { p2pquakeInfo ->
                    emit(
                        EarthquakeInfo(
                            p2pquakeInfo.earthquake.getDatetime(),
                            p2pquakeInfo.earthquake.hypocenter.name,
                            p2pquakeInfo.earthquake.hypocenter.latitude,
                            p2pquakeInfo.earthquake.hypocenter.longitude,
                            p2pquakeInfo.earthquake.hypocenter.magnitude,
                            p2pquakeInfo.earthquake.hypocenter.depth,
                            p2pquakeInfo.points.map { point ->
                                val yahooGeocodeResult = yahooGeocodeRepository.getInfo(
                                    appId = yahooApiAppId,
                                    query = point.addr
                                )
                                if (yahooGeocodeResult.features != null) {
                                    val geometry = yahooGeocodeResult
                                        .features[0]
                                        .geometry
                                    Points(
                                        place = point.addr,
                                        latitude = geometry.getLatitude(),
                                        longitude = geometry.getLongitude(),
                                        scale = point.scale
                                    )
                                } else {
                                    Points(
                                        place = point.addr,
                                        latitude = null,
                                        longitude = null,
                                        scale = point.scale
                                    )
                                }
                            }
                        )
                    )

            }
        }
    }
}