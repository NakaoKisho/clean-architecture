package com.vegcale.architecture.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState

@Composable
fun rememberMarkerState(
    vararg inputs: Any?,
    key: String? = null,
    position: LatLng = LatLng(0.0, 0.0)
): MarkerState = rememberSaveable(
    inputs = inputs,
    saver = MarkerState.Saver,
    key = key,
) {
    MarkerState(position)
}