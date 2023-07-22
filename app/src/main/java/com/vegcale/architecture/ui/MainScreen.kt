package com.vegcale.architecture.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
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

@Composable
internal fun MainScreen(
    items: List<EarthquakeInfo>
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val tokyo = LatLng(35.6812, 139.7671)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(tokyo, 0f)
        }

        GoogleMap(
            cameraPositionState = cameraPositionState
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ArchitectureTheme {
        val noItemList = emptyList<EarthquakeInfo>()
        MainScreen(noItemList)
    }
}