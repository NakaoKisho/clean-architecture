package com.vegcale.architecture.data.model

data class YahooGeocodeInfo(
    val resultInfo: YahooGeocodeInfoResultInfo,
    val features: List<YahooGeocodeInfoFeature>
)

data class YahooGeocodeInfoResultInfo(
    val count: Int,
    val total: Int,
    val start: Int,
    val status: Int,
    val description: String,
    val copyright: String,
    val latency: Float
)

data class YahooGeocodeInfoFeature(
    val id: String,
    val gid: String,
    val name: String,
    val geometry: YahooGeocodeInfoGeometry,
    val category: List<String>,
    val description: String,
    val style: List<String>,
    val property: YahooGeocodeInfoProperty
)

data class YahooGeocodeInfoGeometry(
    val type: String,
    val coordinates: String,
    val boundingBox: String
)

data class YahooGeocodeInfoProperty(
    val uid: String,
    val cassetteId: String,
    val kana: String,
    val country: YahooGeocodeInfoCountry,
    val address: String,
    val governmentCode: String,
    val addressMatchingLevel: String,
    val addressType: String,
    val openForBusiness: String,
    val recursiveQuery: String
)

data class YahooGeocodeInfoCountry(
    val code: String,
    val name: String
)