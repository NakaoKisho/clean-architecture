package com.vegcale.architecture.data.model

data class UserData(
    val isNotificationOn: Boolean,
    val placeIndexes: List<Int>,
    val minIntensityLevelIndex: Int,
)
