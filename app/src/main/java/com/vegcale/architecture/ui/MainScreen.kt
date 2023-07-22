package com.vegcale.architecture.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.ui.theme.ArchitectureTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val items = viewModel.uiState.collectAsStateWithLifecycle()

    if (items.value is MainActivityUiState.Success) {
        MainScreen((items.value as MainActivityUiState.Success).earthquakeData)
    } else {
        val noItemList = emptyList<EarthquakeInfo>()
        MainScreen(noItemList)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MainScreen(
    items: List<EarthquakeInfo>
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BottomSheetScaffold(
            sheetContent = {
                Box(
                    Modifier.fillMaxWidth().height(128.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Swipe up to expand sheet")
                }
            }
        ) {
            // TODO: get location or remember the values
            val tokyo = LatLng(35.6812, 139.7671)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(tokyo, 0f)
            }
            val googleMapUiSettings = MapUiSettings(zoomControlsEnabled = false)

            GoogleMap(
                cameraPositionState = cameraPositionState,
                uiSettings = googleMapUiSettings,
            ) {
                items.forEach {
                    Marker(
                        position = LatLng(it.latitude, it.longitude),
                        title = it.place,
                        snippet = it.place
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ArchitectureTheme {
        val noItemList = emptyList<EarthquakeInfo>()
        MainScreen(noItemList)
    }
}