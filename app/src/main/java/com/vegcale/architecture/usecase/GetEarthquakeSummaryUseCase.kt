package com.vegcale.architecture.usecase

import com.vegcale.architecture.BuildConfig
import com.vegcale.architecture.data.P2pquakeRepository2
import com.vegcale.architecture.data.UserDataRepository
import com.vegcale.architecture.data.YahooGeocodeRepository2
import com.vegcale.architecture.data.model.EarthquakeSummary
import com.vegcale.architecture.data.model.PointDetail
import com.vegcale.architecture.data.model.getDatetime
import com.vegcale.architecture.data.model.getIsoString
import com.vegcale.architecture.data.model.getLatitude
import com.vegcale.architecture.data.model.getLongitude
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import javax.inject.Inject

class GetEarthquakeSummaryUseCase @Inject constructor(
    private val p2pquakeRepository: P2pquakeRepository2,
    private val yahooGeocodeRepository: YahooGeocodeRepository2,
    private val offlineUserDataRepository: UserDataRepository
) {
    operator fun invoke(
        placeNames: List<String>,
        minIntensityLevel: Byte,
        limit: Int = 1,
        offset: Int = 0
    ): Flow<List<EarthquakeSummary>> {
        val yahooApiAppId = BuildConfig.YAHOO_CLIENT_ID
        val timeDifferenceZero = 0
        val timeDifferenceTen = -10

        return flow {
            emit(
                p2pquakeRepository
                    .getInfo(limit, offset).toList()
                    .filter { p2pquakeInfo ->
                        val previousEarthquakeDatetimeText =
                            offlineUserDataRepository.latestEarthquakeDatetime.first()
                        val currentInstant = Clock.System.now()
                        val previousInstant = Instant.parse(p2pquakeInfo.getIsoString())
                        val timeDifference = currentInstant.periodUntil(previousInstant, TimeZone.UTC)
                        val yearDifference = timeDifference.years
                        val monthDifference = timeDifference.months
                        val dayDifference = timeDifference.days
                        val hourDifference = timeDifference.hours
                        val minuteDifference = timeDifference.minutes
                        val previousEarthquakeLatitude =
                            offlineUserDataRepository.latestEarthquakeLatitude.first()
                        val previousEarthquakeLongitude =
                            offlineUserDataRepository.latestEarthquakeLongitude.first()
                        val previousEarthquakeMagnitude =
                            offlineUserDataRepository.latestEarthquakeMagnitude.first()
                        (p2pquakeInfo.earthquake.getDatetime() != previousEarthquakeDatetimeText &&
                                yearDifference == timeDifferenceZero &&
                                monthDifference == timeDifferenceZero &&
                                dayDifference == timeDifferenceZero &&
                                hourDifference == timeDifferenceZero &&
                                minuteDifference >= timeDifferenceTen &&
                                p2pquakeInfo.earthquake.hypocenter.latitude != previousEarthquakeLatitude &&
                                p2pquakeInfo.earthquake.hypocenter.longitude != previousEarthquakeLongitude &&
                                p2pquakeInfo.earthquake.hypocenter.magnitude != previousEarthquakeMagnitude)
                    }
                    .map { p2pquakeInfo ->
                        EarthquakeSummary(
                            datetime = p2pquakeInfo.earthquake.getDatetime(),
                            place = p2pquakeInfo.earthquake.hypocenter.name,
                            latitude = p2pquakeInfo.earthquake.hypocenter.latitude,
                            longitude = p2pquakeInfo.earthquake.hypocenter.longitude,
                            magnitude = p2pquakeInfo.earthquake.hypocenter.magnitude,
                            depth = p2pquakeInfo.earthquake.hypocenter.depth,
                            points = p2pquakeInfo
                                .points
                                .filter {
                                    it.addr.isNotEmpty() &&
                                            placeNames.contains(it.pref) &&
                                            it.scale >= minIntensityLevel
                                }
                                .map { point ->
                                    val yahooGeocodeResult = yahooGeocodeRepository.getInfo(
                                        appId = yahooApiAppId,
                                        query = point.addr
                                    )
                                    if (yahooGeocodeResult.features != null) {
                                        val geometry = yahooGeocodeResult
                                            .features[0]
                                            .geometry
                                        PointDetail(
                                            place = point.addr,
                                            latitude = geometry.getLatitude(),
                                            longitude = geometry.getLongitude(),
                                            scale = point.scale
                                        )
                                    } else {
                                        PointDetail(
                                            place = point.addr,
                                            latitude = null,
                                            longitude = null,
                                            scale = point.scale
                                        )
                                    }
                                }
                        )
                    }
            )
        }
    }
}