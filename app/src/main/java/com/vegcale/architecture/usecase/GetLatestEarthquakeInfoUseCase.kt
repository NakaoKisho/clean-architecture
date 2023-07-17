package com.vegcale.architecture.usecase

import com.vegcale.architecture.data.P2pquakeRepository
import com.vegcale.architecture.data.UsgsEarthquakeRepository
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.getDatetime
import com.vegcale.architecture.data.model.getDepth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class GetLatestEarthquakeInfoUseCase @Inject constructor(
    private val usgsEarthquakeRepository: UsgsEarthquakeRepository,
    private val p2pquakeRepository: P2pquakeRepository,
) {
    operator fun invoke(): Flow<List<EarthquakeInfo>> {
        val usgsEarthquakeFlow =
            usgsEarthquakeRepository
                .getInfo("geojson", 20, "time")
                .map { usgsEarthquakeInfo ->
                    usgsEarthquakeInfo.features
                        .map { feature ->
                        EarthquakeInfo(
                            feature.properties.getDatetime(),
                            feature.properties.place ?: "",
                            feature.geometry.coordinates[1],
                            feature.geometry.coordinates[0],
                            feature.properties.mag,
                            feature.geometry.getDepth(),
                        )
                    }
                }

        val p2pEarthquakeFlow =
            p2pquakeRepository
                .getInfo(10, 0)
                .map { p2pquakeInfo ->
                    p2pquakeInfo.map { data ->
                        EarthquakeInfo(
                            data.earthquake.getDatetime(),
                            data.earthquake.hypocenter.name,
                            data.earthquake.hypocenter.latitude,
                            data.earthquake.hypocenter.longitude,
                            data.earthquake.hypocenter.magnitude,
                            data.earthquake.hypocenter.depth,
                        )
                    }
                }

        return merge(usgsEarthquakeFlow, p2pEarthquakeFlow)
    }
}