package com.vegcale.architecture.data.model

import kotlinx.datetime.Instant.Companion.fromEpochMilliseconds
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
    val milliseconds = fromEpochMilliseconds(time)
    val localDatetime = milliseconds.toLocalDateTime(TimeZone.UTC)
    val year = localDatetime.year.toString()
    val twoDigits = "%02d"
    val month = twoDigits.format(localDatetime.monthNumber)
    val day = twoDigits.format(localDatetime.dayOfMonth)
    val hour = twoDigits.format(localDatetime.hour)
    val minute = twoDigits.format(localDatetime.minute)

    return "${year}/${month}/${day} ${hour}:${minute}"
}

fun UsgsEarthquakeInfoGeometry.getDepth() = coordinates[2].toInt()