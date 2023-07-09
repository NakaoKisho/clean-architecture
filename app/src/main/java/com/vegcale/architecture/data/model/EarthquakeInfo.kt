package com.vegcale.architecture.data.model

/**
 * 地震情報のネットワーク定義
 */
data class EarthquakeInfo(
    val type: String,
    val metadata: EarthquakeInfoMetadata,
    val features: List<EarthquakeInfoFeatures>,
)

data class EarthquakeInfoMetadata(
    val generated: Long,
    val url: String,
    val title: String,
    val status: Short,
    val api: String,
    val limit: Byte,
    val offset: Byte,
    val count: Byte,
)

data class EarthquakeInfoFeatures(
    val type: String,
    val properties: EarthquakeInfoProperties,
    val geometry: EarthquakeInfoGeometry,
    val id: String,
)

data class EarthquakeInfoProperties(
    val mag: Double,
    val place: String,
    val time: Long,
    val updated: Long,
    val tz: Int?,
    val url: String,
    val detail: String,
    val felt: Int?,
    val cdi: Float?,
    val mmi: Float?,
    val alert: String?,
    val status: String,
    val tsunami: Byte,
    val sig: Int,
    val net: String,
    val code: String,
    val ids: String,
    val sources: String,
    val types: String,
    val nst: Int,
    val dmin: Float?,
    val rms: Double,
    val gap: Float,
    val magType: String,
    val type: String,
    val title: String,
)

data class EarthquakeInfoGeometry(
    val type: String,
    val coordinates: List<Double>,
)