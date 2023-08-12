package com.vegcale.architecture.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.maps.android.compose.CameraPositionState

@Composable
inline fun rememberCameraPositionState(
    vararg inputs: Any?,
    key: String? = null,
    crossinline init: CameraPositionState.() -> Unit = {}
): CameraPositionState = rememberSaveable(
    inputs = inputs,
    saver = CameraPositionState.Saver,
    key = key
) {
    CameraPositionState().apply(init)
}