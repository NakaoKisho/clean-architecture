package com.vegcale.architecture.usecase

import com.vegcale.architecture.BuildConfig
import com.vegcale.architecture.data.P2pquakeRepository2
import com.vegcale.architecture.data.YahooGeocodeRepository2
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.Points
import com.vegcale.architecture.data.model.getDatetime
import com.vegcale.architecture.data.model.getLatitude
import com.vegcale.architecture.data.model.getLongitude
import com.vegcale.architecture.data.model.toDrawableResourceId
import com.vegcale.architecture.data.model.toStringResourceId
import javax.inject.Inject

class GetLatestEarthquakeInfoUseCase2 @Inject constructor(
    private val p2pquakeRepository: P2pquakeRepository2,
    private val yahooGeocodeRepository: YahooGeocodeRepository2,
) {
    suspend operator fun invoke(): List<EarthquakeInfo> {
        val yahooApiAppId = BuildConfig.YAHOO_CLIENT_ID

        return p2pquakeRepository
            .getInfo(10, 0)
            .map { p2pquakeInfo ->
                val earthquake = p2pquakeInfo.earthquake
                val hypocenter = earthquake.hypocenter

                EarthquakeInfo(
                    datetime = earthquake.getDatetime(),
                    place = hypocenter.name,
                    latitude = hypocenter.latitude,
                    longitude = hypocenter.longitude,
                    magnitude = hypocenter.magnitude,
                    depth = hypocenter.depth,
                    points = p2pquakeInfo.points.map { point ->
                        val yahooGeocodeResult = yahooGeocodeRepository.getInfo(
                            appId = yahooApiAppId,
                            query = point.addr
                        )
                        val geometry = yahooGeocodeResult.features?.firstOrNull()?.geometry

                        Points(
                            place = point.addr,
                            latitude = geometry?.getLatitude(),
                            longitude = geometry?.getLongitude(),
                            scaleIcon = point.toDrawableResourceId(),
                            scale = point.toStringResourceId()
                        )
                    }
                )
            }
    }
}