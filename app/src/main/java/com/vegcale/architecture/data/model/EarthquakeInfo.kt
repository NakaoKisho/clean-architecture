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
    val address: String?,
    val scale: Byte?,
): Serializable

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