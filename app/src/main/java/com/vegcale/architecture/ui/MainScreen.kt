package com.vegcale.architecture.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.google.maps.android.compose.rememberMarkerState
import com.vegcale.architecture.R
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.EarthquakeInfoMapSaver
import com.vegcale.architecture.ui.components.DefaultDetailMapUiSettings
import com.vegcale.architecture.ui.components.rememberCameraPositionState
import com.vegcale.architecture.ui.components.rememberMarkerState
import com.vegcale.architecture.ui.theme.ArchitectureTheme
import com.vegcale.architecture.util.BitmapHelper

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    items: List<EarthquakeInfo>
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        var itemInfo by rememberSaveable(stateSaver = EarthquakeInfoMapSaver) {
            mutableStateOf(
                EarthquakeInfo(
                    datetime = "",
                    place = "",
                    latitude = 0.0,
                    longitude = 0.0,
                    magnitude = 0.0,
                    depth = 0
                )
            )
        }
        var sheetPeekHeight by rememberSaveable { mutableFloatStateOf(0f) }

        BottomSheetScaffold(
            sheetContent = { BottomSheetContent(itemInfo) },
            sheetPeekHeight = sheetPeekHeight.dp
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
                onMapClick = { sheetPeekHeight = 0f }
            ) {
                items.forEach { earthquakeInfoItem ->
                    val markerState = rememberMarkerState(
                        position = LatLng(
                            earthquakeInfoItem.latitude,
                            earthquakeInfoItem.longitude
                        )
                    )
                    Marker(
                        state = markerState,
                        title = earthquakeInfoItem.place,
                        snippet = earthquakeInfoItem.place,
                        onClick = { _ ->
                            itemInfo = earthquakeInfoItem
                            sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight.value
                            false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetContent(itemInfo: EarthquakeInfo) {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        topBar = {
            TopAppBar(
                title = { Text(itemInfo.place) }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            val latLng = LatLng(itemInfo.latitude, itemInfo.longitude)
            val cameraPositionState = rememberCameraPositionState(
                inputs = arrayOf(latLng.toString()),
            ) {
                position = CameraPosition.fromLatLngZoom(latLng, 6.5f)
            }
            val googleMapUiSettings = DefaultDetailMapUiSettings

            GoogleMap(
                modifier = Modifier.weight(7.0f),
                cameraPositionState = cameraPositionState,
                uiSettings = googleMapUiSettings,
            ) {
                val markerState = rememberMarkerState(
                    inputs = arrayOf(latLng.toString()),
                    position = latLng
                )
                val markerImage =
                    BitmapHelper().vectorToBitmap(id = R.drawable.baseline_epicenter_24)

                Marker(
                    state = markerState,
                    icon = markerImage
                )
            }
            Text(
                modifier = Modifier.weight(1.0f),
                text = itemInfo.datetime
            )
            Text(
                modifier = Modifier.weight(1.0f),
                text = itemInfo.latitude.toString()
            )
            Text(
                modifier = Modifier.weight(2.0f),
                text = itemInfo.depth.toString()
            )
            Text(
                modifier = Modifier.weight(2.0f),
                text = itemInfo.magnitude.toString()
            )
            Text(
                modifier = Modifier.weight(2.0f),
                text = itemInfo.longitude.toString()
            )
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

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    ArchitectureTheme {
        val earthquakeInfo = EarthquakeInfo(
            datetime = "2023年 01月 01日 01:00",
            place = "テスト県",
            latitude = 10.1,
            longitude = 10.1,
            magnitude = 5.0,
            depth = 10
        )
        BottomSheetContent(earthquakeInfo)
    }
}