package com.vegcale.architecture.data.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * 地震情報のネットワーク定義
 */
data class UsgsEarthquakeInfo(
    val type: String,
    val metadata: UsgsEarthquakeInfoMetadata,
    val features: List<UsgsEarthquakeInfoFeatures>,
)

data class UsgsEarthquakeInfoMetadata(
    val generated: Long,
    val url: String,
    val title: String,
    val status: Short,
    val api: String,
    val limit: Byte,
    val offset: Byte,
    val count: Byte,
)

data class UsgsEarthquakeInfoFeatures(
    val type: String,
    val properties: UsgsEarthquakeInfoProperties,
    val geometry: UsgsEarthquakeInfoGeometry,
    val id: String,
)

data class UsgsEarthquakeInfoProperties(
    val mag: Double,
    val place: String?,
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
    val nst: Int?,
    val dmin: Float?,
    val rms: Double,
    val gap: Float?,
    val magType: String,
    val type: String,
    val title: String,
)

data class UsgsEarthquakeInfoGeometry(
    val type: String,
    val coordinates: List<Double>,
)

fun UsgsEarthquakeInfoProperties.getDatetime(): String {
    val localDatetime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern("yyyy年 MM月 dd日 hh:mm")

    return localDatetime.format(formatter)
}

fun UsgsEarthquakeInfoGeometry.getDepth() = coordinates[2].toInt()