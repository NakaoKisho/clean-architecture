package com.vegcale.architecture.data.model

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
    val scaleIcon: Int,
    val scale: Int
): Serializable
