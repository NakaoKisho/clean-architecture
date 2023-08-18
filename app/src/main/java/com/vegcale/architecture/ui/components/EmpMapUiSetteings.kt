package com.vegcale.architecture.ui.components

import com.google.maps.android.compose.MapUiSettings

val DefaultDetailMapUiSettings = MapUiSettings(
    compassEnabled = false,
    indoorLevelPickerEnabled = false,
    mapToolbarEnabled = false,
    myLocationButtonEnabled = false,
    rotationGesturesEnabled = true,
    scrollGesturesEnabled = true,
    scrollGesturesEnabledDuringRotateOrZoom = true,
    tiltGesturesEnabled = true,
    zoomControlsEnabled = false,
    zoomGesturesEnabled = true,
)