package com.vegcale.architecture.data.model

import com.vegcale.architecture.R
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

fun P2pquakeInfoPoint.toStringResourceId() =
    when (this.scale.toInt()) {
        0 -> R.string.no_data
        10 -> R.string.seismic_intensity_of_one
        20 -> R.string.seismic_intensity_of_two
        30 -> R.string.seismic_intensity_of_three
        40 -> R.string.seismic_intensity_of_four
        45 -> R.string.seismic_intensity_of_lower_five
        46 -> R.string.seismic_intensity_of_more_than_upper_five
        50 -> R.string.seismic_intensity_of_upper_five
        55 -> R.string.seismic_intensity_of_lower_six
        60 -> R.string.seismic_intensity_of_upper_six
        70 -> R.string.seismic_intensity_of_seven
        else -> R.string.no_data
    }

fun P2pquakeInfoPoint.toDrawableResourceId() =
    when (this.scale.toInt()) {
        0 -> R.drawable.baseline_question_mark_24
        10 -> R.drawable.seismic_intensity_of_one
        20 -> R.drawable.seismic_intensity_of_two
        30 -> R.drawable.seismic_intensity_of_three
        40 -> R.drawable.seismic_intensity_of_four
        45 -> R.drawable.seismic_intensity_of_lower_five
        46 -> R.drawable.seismic_intensity_of_more_than_upper_five
        50 -> R.drawable.seismic_intensity_of_upper_five
        55 -> R.drawable.seismic_intensity_of_lower_six
        60 -> R.drawable.seismic_intensity_of_upper_six
        70 -> R.drawable.seismic_intensity_of_seven
        else -> R.drawable.baseline_question_mark_24
    }

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