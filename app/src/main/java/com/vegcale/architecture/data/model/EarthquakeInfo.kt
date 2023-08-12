package com.vegcale.architecture.data.model

import androidx.compose.runtime.saveable.mapSaver

data class EarthquakeInfo(
    val datetime: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val magnitude: Double,
    val depth: Int,
)

val EarthquakeInfoMapSaver = mapSaver(
    save = {
        mapOf(
            "datetime" to it.datetime,
            "place" to it.place,
            "latitude" to it.latitude,
            "longitude" to it.longitude,
            "magnitude" to it.magnitude,
            "depth" to it.depth
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
        )
    }
)