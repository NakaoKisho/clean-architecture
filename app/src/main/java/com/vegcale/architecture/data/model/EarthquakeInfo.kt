package com.vegcale.architecture.data.model

import androidx.compose.runtime.saveable.mapSaver
import java.io.Serializable

data class EarthquakeInfo(
    val datetime: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val magnitude: Double,
    val depth: Int,
    val points: List<Points>,
)

data class Points(
    val place: String?,
    val latitude: Double?,
    val longitude: Double?,
    val scale: Byte?,
): Serializable

object SeismicIntensity {
    const val IntensityOfOne: Byte = 10
    const val IntensityOfTwo: Byte = 20
    const val IntensityOfThree: Byte = 30
    const val IntensityOfFour: Byte = 40
    const val IntensityOfLowerFive: Byte = 45
    const val IntensityOfMoreThanUpperFive: Byte = 46
    const val IntensityOfUpperFive: Byte = 50
    const val IntensityOfLowerSix: Byte = 55
    const val IntensityOfUpperSix: Byte = 60
    const val IntensityOfSeven: Byte = 70
}

@Suppress("UNCHECKED_CAST")
val EarthquakeInfoMapSaver = mapSaver(
    save = {
        mapOf(
            "datetime" to it.datetime,
            "place" to it.place,
            "latitude" to it.latitude,
            "longitude" to it.longitude,
            "magnitude" to it.magnitude,
            "depth" to it.depth,
            "points" to it.points
        )
    },
    restore = {
        EarthquakeInfo(
            datetime = it["datetime"] as String,
            place = it["place"] as String,
            latitude = it["latitude"] as Double,
            longitude = it["longitude"] as Double,
            magnitude = it["magnitude"] as Double,
            depth = it["depth"] as Int,
            points = it["points"] as List<Points>,
        )
    }
)