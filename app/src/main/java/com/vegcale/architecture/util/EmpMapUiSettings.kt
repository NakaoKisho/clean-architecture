package com.vegcale.architecture.util

import com.google.maps.android.compose.MapUiSettings

val DefaultSummaryMapUiSettings = MapUiSettings(
    compassEnabled = true,
    indoorLevelPickerEnabled = true,
    mapToolbarEnabled = true,
    myLocationButtonEnabled = true,
    rotationGesturesEnabled = true,
    scrollGesturesEnabled = true,
    scrollGesturesEnabledDuringRotateOrZoom = true,
    tiltGesturesEnabled = true,
    zoomControlsEnabled = true,
    zoomGesturesEnabled = true,
)

val DefaultDetailMapUiSettings = MapUiSettings(
    compassEnabled = false,
    indoorLevelPickerEnabled = false,
    mapToolbarEnabled = true,
    myLocationButtonEnabled = false,
    rotationGesturesEnabled = false,
    scrollGesturesEnabled = false,
    scrollGesturesEnabledDuringRotateOrZoom = false,
    tiltGesturesEnabled = false,
    zoomControlsEnabled = true,
    zoomGesturesEnabled = false,
)