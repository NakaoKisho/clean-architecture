package com.vegcale.architecture.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.maps.android.compose.CameraPositionState

@Composable
inline fun rememberEmpCameraPositionState(
    vararg inputs: Any?,
    key: String? = null,
    crossinline init: CameraPositionState.() -> Unit = {}
): CameraPositionState = rememberSaveable(
    inputs = inputs,
    key = key,
    saver = CameraPositionState.Saver
) {
    CameraPositionState().apply(init)
}
