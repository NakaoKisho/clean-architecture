package com.vegcale.architecture.data.model

data class EarthquakeInfo(
    val datetime: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val magnitude: Double,
    val depth: Int,
)