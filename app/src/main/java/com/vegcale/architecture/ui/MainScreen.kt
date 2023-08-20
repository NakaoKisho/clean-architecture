package com.vegcale.architecture.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.vegcale.architecture.data.model.Points
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
                    depth = 0,
                    points = listOf(Points("",0.0,0.0, 10))
                )
            )
        }
        var sheetPeekHeight by rememberSaveable { mutableFloatStateOf(0f) }

        BottomSheetScaffold(
            sheetContent = { BottomSheetContent(itemInfo) },
            sheetPeekHeight = sheetPeekHeight.dp,
            sheetDragHandle = { BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.primary
            ) }
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

@Composable
private fun BottomSheetContent(itemInfo: EarthquakeInfo) {
    Column {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = itemInfo.place,
                modifier = Modifier.weight(1.0f),
                fontSize = 30.sp
            )
            Text(
                text = itemInfo.datetime,
                fontSize = 15.sp
            )
        }

        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    val epicenterLatLng = LatLng(itemInfo.latitude, itemInfo.longitude)
                    val cameraPositionState = rememberCameraPositionState(
                        inputs = arrayOf(epicenterLatLng.toString()),
                    ) {
                        position = CameraPosition.fromLatLngZoom(epicenterLatLng, 6.0f)
                    }
                    val googleMapUiSettings = DefaultDetailMapUiSettings

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.5f),
                        cameraPositionState = cameraPositionState,
                        uiSettings = googleMapUiSettings,
                    ) {
                        val epicenterMarkerState = rememberMarkerState(
                            inputs = arrayOf(epicenterLatLng.toString()),
                            position = epicenterLatLng
                        )
                        val bimapHelper = BitmapHelper()
                        val markerImage = bimapHelper.vectorToBitmap(id = R.drawable.baseline_epicenter_24)

                        // Marker for the hypocenter
                        Marker(
                            state = epicenterMarkerState,
                            icon = markerImage,
                            title = stringResource(R.string.epicenter)
                        )

                        // Marker for place observes the earthquake
                        itemInfo.points.forEach { point ->
                            if (point.latitude == null || point.longitude == null) return@forEach

                            val observationPlaceLatLng = LatLng(point.latitude, point.longitude)
                            val observationPlaceMarkerState = rememberMarkerState(
                                inputs = arrayOf(observationPlaceLatLng.toString()),
                                position = observationPlaceLatLng
                            )
                            val (seismicIntensity, seismicIntensityVectorId) = when(point.scale) {
                                10.toByte() -> stringResource(R.string.intensity_of_one) to R.drawable.intensity_of_one
                                20.toByte() -> stringResource(R.string.intensity_of_two) to R.drawable.intensity_of_two
                                30.toByte() -> stringResource(R.string.intensity_of_three) to R.drawable.intensity_of_three
                                40.toByte() -> stringResource(R.string.intensity_of_four) to R.drawable.intensity_of_four
                                45.toByte() -> stringResource(R.string.intensity_of_lower_five) to R.drawable.intensity_of_lower_five
                                46.toByte() -> stringResource(R.string.intensity_of_more_than_upper_five) to R.drawable.intensity_of_more_than_upper_five
                                50.toByte() -> stringResource(R.string.intensity_of_upper_five) to R.drawable.intensity_of_upper_five
                                55.toByte() -> stringResource(R.string.intensity_of_lower_six) to R.drawable.intensity_of_lower_six
                                60.toByte() -> stringResource(R.string.intensity_of_upper_six) to R.drawable.intensity_of_upper_six
                                else -> stringResource(R.string.intensity_of_seven) to R.drawable.intensity_of_seven
                            }

                            Marker(
                                state = observationPlaceMarkerState,
                                icon = bimapHelper.vectorToBitmap(id = seismicIntensityVectorId),
                                snippet = seismicIntensity,
                                title = point.place
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.epicenter),
                            modifier = Modifier.weight(1.0f)
                        )
                        Text(
                            itemInfo.place,
                            modifier = Modifier.weight(2.0f)
                        )
//                        val tileModifier = Modifier.weight(1.0f)
//
//                        EmpTile(
//                            detailFontSize = 24.sp,
//                            detailText = itemInfo.place,
//                            modifier = tileModifier.background(colorResource(R.color.green_800)),
//                            titleFontSize = 24.sp,
//                            titleText = stringResource(R.string.epicenter)
//                        )
//                        EmpTile(
//                            detailFontSize = 24.sp,
//                            detailText = itemInfo.magnitude.toString(),
//                            modifier = tileModifier.background(colorResource(R.color.red_800)),
//                            titleFontSize = 24.sp,
//                            titleText = stringResource(R.string.magnitude)
//                        )
                    }
                }

                item {
                    Text(
                        text = "itemInfo.depth"
                    )
                    Text(
                        text = itemInfo.depth.toString()
                    )
                }

                item {
                    Text(
                        text = "itemInfo.latitude"
                    )
                    Text(
                        text = itemInfo.latitude.toString()
                    )
                }

                item {
                    Text(
                        text = "itemInfo.longitude"
                    )
                    Text(
                        text = itemInfo.longitude.toString()
                    )
                }

                item {
                    Text(
                        text = "itemInfo.points"
                    )
                    Text(
                        text = itemInfo.points.toString()
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

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    ArchitectureTheme {
        val earthquakeInfo = EarthquakeInfo(
            datetime = "2023/01/01/ 01:00",
            place = "テスト県",
            latitude = 10.1,
            longitude = 10.1,
            magnitude = 5.0,
            depth = 10,
            points = listOf(Points("テスト場所",0.0,0.0, 10))
        )
        BottomSheetContent(earthquakeInfo)
    }
}