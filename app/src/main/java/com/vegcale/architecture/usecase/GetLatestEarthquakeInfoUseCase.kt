package com.vegcale.architecture.usecase

import com.vegcale.architecture.data.P2pquakeRepository
import com.vegcale.architecture.data.Result
import com.vegcale.architecture.data.UsgsEarthquakeRepository
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.Points
import com.vegcale.architecture.data.model.getDatetime
import com.vegcale.architecture.data.model.getDepth
import com.vegcale.architecture.data.succeeded
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLatestEarthquakeInfoUseCase @Inject constructor(
    private val usgsEarthquakeRepository: UsgsEarthquakeRepository,
    private val p2pquakeRepository: P2pquakeRepository,
) {
    operator fun invoke(): Flow<List<EarthquakeInfo>> {
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
                                listOf(Points("","",1))
                            )
                        }
                }

                val p2pResult = p2pquakeRepository.getInfo(10, 0)
                var p2pList: List<EarthquakeInfo> = emptyList()
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
                                    Points(
                                        place = it.pref,
                                        address = it.addr,
                                        scale = it.scale
                                    )
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