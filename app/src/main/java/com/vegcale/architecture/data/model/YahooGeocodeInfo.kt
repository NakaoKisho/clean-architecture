package com.vegcale.architecture.data.model

import com.google.gson.annotations.SerializedName

data class YahooGeocodeInfo(
    @SerializedName("ResultInfo") val resultInfo: YahooGeocodeInfoResultInfo?,
    @SerializedName("Feature") val features: List<YahooGeocodeInfoFeature>?
)

data class YahooGeocodeInfoResultInfo(
    @SerializedName("Count") val count: Int,
    @SerializedName("Total") val total: Int,
    @SerializedName("Start") val start: Int,
    @SerializedName("Status") val status: Int,
    @SerializedName("Description") val description: String,
    @SerializedName("Copyright") val copyright: String,
    @SerializedName("Latency") val latency: Float
)

data class YahooGeocodeInfoFeature(
    @SerializedName("Id") val id: String,
    @SerializedName("Gid") val gid: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Geometry") val geometry: YahooGeocodeInfoGeometry,
    @SerializedName("Category") val category: List<String>,
    @SerializedName("Description") val description: String,
    @SerializedName("Style") val style: List<String>,
    @SerializedName("Property") val property: YahooGeocodeInfoProperty
)

data class YahooGeocodeInfoGeometry(
    @SerializedName("Type") val type: String,
    @SerializedName("Coordinates") val coordinates: String,
    @SerializedName("BoundingBox") val boundingBox: String
)

data class YahooGeocodeInfoProperty(
    @SerializedName("Uid") val uid: String,
    @SerializedName("CassetteId") val cassetteId: String,
    @SerializedName("Kana") val kana: String,
    @SerializedName("Country") val country: YahooGeocodeInfoCountry,
    @SerializedName("Address") val address: String,
    @SerializedName("GovernmentCode") val governmentCode: String,
    @SerializedName("AddressMatchingLevel") val addressMatchingLevel: String,
    @SerializedName("AddressType") val addressType: String,
    @SerializedName("OpenForBusiness") val openForBusiness: String,
    @SerializedName("RecursiveQuery") val recursiveQuery: String
)

data class YahooGeocodeInfoCountry(
    @SerializedName("Code") val code: String,
    @SerializedName("Name") val name: String
)

fun YahooGeocodeInfoGeometry.getLatitude(): Double {
    val coordinates = coordinates.split(",")
    return coordinates[1].toDouble()
}

fun YahooGeocodeInfoGeometry.getLongitude(): Double {
    val coordinates = coordinates.split(",")
    return coordinates[0].toDouble()
}