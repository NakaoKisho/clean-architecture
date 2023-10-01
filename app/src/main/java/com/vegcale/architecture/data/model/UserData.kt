package com.vegcale.architecture.data.model

data class UserData(
    val isNotificationOn: Boolean,
    val places: List<String>,
    val minIntensityLevelIndex: Int,
)
