package com.vegcale.architecture.data.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class P2pquakeInfo(
    val id: String,
    val code: Short,
    val time: String,
    val issue: P2pquakeInfoIssue,
    val earthquake: P2pquakeInfoEarthquake,
    val points: List<P2pquakeInfoPoint>,
)

data class P2pquakeInfoIssue(
    val source: String,
    val time: String,
    val type: String,
    val correct: String,
)

data class P2pquakeInfoEarthquake(
    val time: String,
    val hypocenter: P2pquakeInfoHypocenter,
    val maxScale: Byte,
    val domesticTsunami: String,
    val foreignTsunami: String,
)

data class P2pquakeInfoHypocenter(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val depth: Int,
    val magnitude: Double,
)

data class P2pquakeInfoPoint(
    val pref: String,
    val addr: String,
    val isArea: Boolean,
    val scale: Byte,
)

fun P2pquakeInfoEarthquake.getDatetime(): String {
    val dataFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    val localDatetime = LocalDateTime.parse(time, dataFormatter)
    val displayFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")

    return localDatetime.format(displayFormatter)
}

fun P2pquakeInfo.getIsoString(): String =
    time
        .replace("/", "-")
        .replace(" ", "T")
        .plus("+09:00")