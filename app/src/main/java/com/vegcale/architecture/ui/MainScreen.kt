package com.vegcale.architecture.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.vegcale.architecture.R
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.EarthquakeInfoMapSaver
import com.vegcale.architecture.data.model.Points
import com.vegcale.architecture.data.model.SeismicIntensity
import com.vegcale.architecture.ui.components.EmpProgressIndicator
import com.vegcale.architecture.ui.theme.ArchitectureTheme
import com.vegcale.architecture.util.BitmapHelper
import com.vegcale.architecture.util.DefaultDetailMapUiSettings
import com.vegcale.architecture.util.DefaultSummaryMapUiSettings
import com.vegcale.architecture.util.isPermissionGranted
import com.vegcale.architecture.util.rememberEmpCameraPositionState
import com.vegcale.architecture.util.rememberEmpMarkerState
import kotlinx.coroutines.launch

private const val TAG = "MainScreen.kt"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    // This should take earthquake info independently
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val itemState = state.earthquakeData.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val items = remember { mutableStateListOf<EarthquakeInfo>() }
    val loadErrorMessage = stringResource(R.string.load_error)
    Log.i(TAG, itemState.value.toString())
    when (itemState.value) {
        is MainActivityUiState.Success -> {
            (itemState.value as MainActivityUiState.Success).earthquakeData.forEach {
                if (items.contains(it)) return@forEach
                items.add(it)
            }
        }

        is MainActivityUiState.Error -> {
            LaunchedEffect(key1 = null) {
                snackbarHostState.showSnackbar(
                    message = loadErrorMessage
                )
            }
        }

        is MainActivityUiState.Loading -> {
            EmpProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2.0f)
                    .clickable {}
            )
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1.0f),
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
            ) },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val deniedLocationPermissionMessage = stringResource(R.string.denied_location_permission)
            val goToSettingsMessage = stringResource(R.string.go_to_settings)
            val cannotResolveActivityErrorMessage = stringResource(R.string.cannot_resolve_activity)
            val requestLocationPermissions =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissionsMap ->
                    if (permissionsMap.all { !it.value }) {
                        coroutineScope.launch {
                            val snackbarActionResult = snackbarHostState.showSnackbar(
                                message = deniedLocationPermissionMessage,
                                actionLabel = goToSettingsMessage
                            )
                            if (snackbarActionResult == SnackbarResult.ActionPerformed) {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", context.packageName, null)
                                )
                                if (intent.resolveActivity(context.packageManager) != null) {
                                    ContextCompat.startActivity(context, intent, null)
                                } else {
                                    Log.e(TAG, cannotResolveActivityErrorMessage)
                                }
                            }
                        }
                    } else {
                        for (key in permissionsMap.keys) {
                            val isGranted = permissionsMap[key]
                            if (isGranted == null || !isGranted) continue

                            viewModel.onPermissionChange(
                                permission = key,
                                isGranted = isGranted
                            )
                            viewModel.updateLocation()
                            break
                        }
                    }
                }
            var showExplanationDialogForLocationPermission by remember { mutableStateOf(false) }
            if (showExplanationDialogForLocationPermission) {
                when {
                    isPermissionGranted(context, ACCESS_COARSE_LOCATION) ||
                            isPermissionGranted(context, ACCESS_FINE_LOCATION) -> {
                        requestLocationPermissions.launch(arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION))
                        showExplanationDialogForLocationPermission = false
                    }
                    else ->
                        LocationExplanationDialog(
                            onConfirm = {
                                requestLocationPermissions.launch(arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION))
                                showExplanationDialogForLocationPermission = false
                            },
                            onDismiss = { showExplanationDialogForLocationPermission = false },
                        )
                }
            }

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(state.latLng, 0f)
            }
            val googleMapUiSettings = DefaultSummaryMapUiSettings

            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = state.hasLocationAccess),
                uiSettings = googleMapUiSettings,
                onMapClick = { sheetPeekHeight = 0f }
            ) {
                items.forEach { earthquakeInfoItem ->
                    if (earthquakeInfoItem.latitude.toFloat() == 0.0f &&
                        earthquakeInfoItem.longitude.toFloat() == 0.0f) {
                        return@forEach
                    }

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

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                AnimatedVisibility(
                    visible = !state.hasLocationAccess,
                    exit = shrinkHorizontally() + fadeOut()
                )  {
                    SmallFloatingActionButton(
                        onClick = {
                            showExplanationDialogForLocationPermission = true
                        },
                        modifier = Modifier.padding(
                            top = 8.dp,
                            end = 8.dp
                        ),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_navigation_24),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationExplanationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.location_access)) },
        text = { Text(stringResource(R.string.location_access_on_map_reason)) },
        icon = {
            Icon(
                Icons.Filled.Done,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.no))
            }
        }
    )
}

@Composable
private fun BottomSheetContent(itemInfo: EarthquakeInfo) {
    Column(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        BottomSheetHeader(
            timestampText = itemInfo.datetime,
            titleText = itemInfo.place
        )

        Divider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            thickness = 0.dp,
            color = Color.Transparent
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                val epicenterLatLng = LatLng(itemInfo.latitude, itemInfo.longitude)
                val cameraPositionState = rememberEmpCameraPositionState(
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
                    val epicenterMarkerState = rememberEmpMarkerState(
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
                        val observationPlaceMarkerState = rememberEmpMarkerState(
                            inputs = arrayOf(observationPlaceLatLng.toString()),
                            position = observationPlaceLatLng
                        )
                        val (seismicIntensity, seismicIntensityVectorId) = when (point.scale) {
                            SeismicIntensity.IntensityOfOne ->
                                stringResource(R.string.seismic_intensity_of_one) to R.drawable.seismic_intensity_of_one
                            SeismicIntensity.IntensityOfTwo ->
                                stringResource(R.string.seismic_intensity_of_two) to R.drawable.seismic_intensity_of_two
                            SeismicIntensity.IntensityOfThree ->
                                stringResource(R.string.seismic_intensity_of_three) to R.drawable.seismic_intensity_of_three
                            SeismicIntensity.IntensityOfFour ->
                                stringResource(R.string.seismic_intensity_of_four) to R.drawable.seismic_intensity_of_four
                            SeismicIntensity.IntensityOfLowerFive ->
                                stringResource(R.string.seismic_intensity_of_lower_five) to R.drawable.seismic_intensity_of_lower_five
                            SeismicIntensity.IntensityOfMoreThanUpperFive ->
                                stringResource(R.string.seismic_intensity_of_more_than_upper_five) to R.drawable.seismic_intensity_of_more_than_upper_five
                            SeismicIntensity.IntensityOfUpperFive ->
                                stringResource(R.string.seismic_intensity_of_upper_five) to R.drawable.seismic_intensity_of_upper_five
                            SeismicIntensity.IntensityOfLowerSix ->
                                stringResource(R.string.seismic_intensity_of_lower_six) to R.drawable.seismic_intensity_of_lower_six
                            SeismicIntensity.IntensityOfUpperSix ->
                                stringResource(R.string.seismic_intensity_of_upper_six) to R.drawable.seismic_intensity_of_upper_six
                            SeismicIntensity.IntensityOfSeven ->
                                stringResource(R.string.seismic_intensity_of_seven) to R.drawable.seismic_intensity_of_seven
                            else ->
                                stringResource(R.string.no_data) to R.drawable.baseline_question_mark_24
                        }

                        Marker(
                            state = observationPlaceMarkerState,
                            icon = bimapHelper.vectorToBitmap(id = seismicIntensityVectorId),
                            snippet = seismicIntensity,
                            title = point.place
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    thickness = 0.dp,
                    color = Color.Transparent
                )

                val tableBaseModifier = Modifier
                    .height(30.dp)
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.light_gray)
                    )
                    .padding(start = 8.dp)
                    .layout { measurable, constraints ->
                        val placeable =
                            measurable.measure(
                                constraints.copy(minWidth = 0, minHeight = 0)
                            )
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            val x = 0
                            val y = (constraints.maxHeight - placeable.height) / 2
                            placeable.placeRelative(x, y)
                        }
                    }
                val tableHeaderModifier = Modifier
                    .background(colorResource(R.color.pale_gray))
                    .then(tableBaseModifier)
                val tableDataModifier = Modifier
                    .then(tableBaseModifier)

                EpicenterDataTable(
                    itemInfo,
                    tableDataModifier,
                    tableHeaderModifier
                )

                Divider(
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    thickness = 0.dp,
                    color = Color.Transparent
                )

                ObservationPlacesDataTable(
                    itemInfo.points,
                    tableDataModifier,
                    tableHeaderModifier
                )
            }
        }
    }
}

@Composable
private fun BottomSheetHeader(
    timestampText: String,
    titleText: String
) {
    Text(
        text = titleText,
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 30.sp
    )
    Text(
        text = timestampText,
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 15.sp,
        textAlign = TextAlign.End
    )
}

@Composable
private fun EpicenterDataTable(
    itemInfo: EarthquakeInfo,
    @SuppressLint("ModifierParameter") tableDataModifier: Modifier,
    @SuppressLint("ModifierParameter") tableHeaderModifier: Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.epicenter),
            modifier = tableHeaderModifier.weight(1.5f)
        )
        Text(
            text = itemInfo.place,
            modifier = tableDataModifier.weight(2.0f)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.magnitude),
            modifier = tableHeaderModifier.weight(1.5f)
        )
        Text(
            text = itemInfo.magnitude.toString(),
            modifier = tableDataModifier.weight(2.0f)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.depth),
            modifier = tableHeaderModifier.weight(1.5f)
        )
        Text(
            text = itemInfo.depth.toString(),
            modifier = tableDataModifier.weight(2.0f)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.latitude),
            modifier = tableHeaderModifier.weight(1.5f)
        )
        Text(
            text = itemInfo.latitude.toString(),
            modifier = tableDataModifier.weight(2.0f)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.longitude),
            modifier = tableHeaderModifier.weight(1.5f)
        )
        Text(
            text = itemInfo.longitude.toString(),
            modifier = tableDataModifier.weight(2.0f)
        )
    }
}

@Composable
private fun ObservationPlacesDataTable(
    points: List<Points>,
    @SuppressLint("ModifierParameter") tableDataModifier: Modifier,
    @SuppressLint("ModifierParameter") tableHeaderModifier: Modifier
) {
    Text(
        text = stringResource(R.string.observation_places_info),
        modifier = Modifier.fillMaxWidth()
    )

    val noData = stringResource(R.string.no_data)
    points.forEach { point ->
        if (point.place == null || point.place == "") return@forEach

        var isExpanded by rememberSaveable { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.observation_place_name),
                modifier = tableHeaderModifier.weight(1.5f)
            )
            Box(
                modifier = tableDataModifier.weight(2.0f)
            ) {
                Row(
                    modifier = Modifier.clickable {
                        isExpanded = !isExpanded
                    }
                ) {
                    Text(
                        text = point.place,
                        modifier = Modifier.weight(2.0f)
                    )
                    Icon(
                        painter = if (!isExpanded) {
                            painterResource(R.drawable.baseline_expand_more_24)
                        } else {
                            painterResource(R.drawable.baseline_expand_less_24)
                        },
                        contentDescription = if (!isExpanded) {
                            stringResource(R.string.expand_more)
                        } else {
                            stringResource(R.string.expand_less)
                        }
                    )
                }
            }
        }

        val sizeInPx = with(LocalDensity.current) { -40.dp.roundToPx() }
        AnimatedVisibility(
            visible = isExpanded,
            enter = slideInVertically {
                // Slide in from 40 dp from the top.
                sizeInPx
            } + expandVertically(
                // Expand from the top.
                expandFrom = Alignment.Top
            ) + fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        )  {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.seismic_intensity),
                        modifier = tableHeaderModifier.weight(1.5f)
                    )
                    val scaleText = if (point.scale == null) {
                        noData
                    } else {
                        when (point.scale) {
                            SeismicIntensity.IntensityOfOne -> stringResource(R.string.seismic_intensity_level_one)
                            SeismicIntensity.IntensityOfTwo -> stringResource(R.string.seismic_intensity_level_two)
                            SeismicIntensity.IntensityOfThree -> stringResource(R.string.seismic_intensity_level_three)
                            SeismicIntensity.IntensityOfFour -> stringResource(R.string.seismic_intensity_level_four)
                            SeismicIntensity.IntensityOfLowerFive -> stringResource(R.string.seismic_intensity_level_lower_five)
                            SeismicIntensity.IntensityOfMoreThanUpperFive -> stringResource(R.string.seismic_intensity_level_more_than_upper_five)
                            SeismicIntensity.IntensityOfUpperFive -> stringResource(R.string.seismic_intensity_level_upper_five)
                            SeismicIntensity.IntensityOfLowerSix -> stringResource(R.string.seismic_intensity_level_lower_six)
                            SeismicIntensity.IntensityOfUpperSix -> stringResource(R.string.seismic_intensity_level_upper_six)
                            SeismicIntensity.IntensityOfSeven -> stringResource(R.string.seismic_intensity_level_seven)
                            else -> stringResource(R.string.no_data)
                        }
                    }
                    Text(
                        text = scaleText,
                        modifier = tableDataModifier.weight(2.0f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.latitude),
                        modifier = tableHeaderModifier.weight(1.5f)
                    )
                    Text(
                        text =
                        if (point.latitude != null) point.latitude.toString() else noData,
                        modifier = tableDataModifier.weight(2.0f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.longitude),
                        modifier = tableHeaderModifier.weight(1.5f)
                    )
                    Text(
                        text =
                        if (point.longitude != null) point.longitude.toString() else noData,
                        modifier = tableDataModifier.weight(2.0f)
                    )
                }
            }
        }

        Divider(
            modifier = Modifier.padding(top = 1.dp, bottom = 1.dp),
            thickness = 0.dp,
            color = Color.Transparent
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    ArchitectureTheme {
//        val noItemList = emptyList<EarthquakeInfo>()
//        MainScreen(noItemList)
//    }
//}

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
            points = listOf(
                Points("テスト場所",1.0,1.0, 10),
                Points("テスト場所",2.0,2.0, 20),
                Points("テスト場所",2.0,2.0, 20),
                Points("テスト場所",3.0,3.0, 30),
                Points("テスト場所",3.0,3.0, 30),
                Points("テスト場所",3.0,3.0, 30),
                Points("テスト場所",4.0,4.0, 40),
                Points("テスト場所",4.0,4.0, 40),
                Points("テスト場所",4.0,4.0, 40),
                Points("テスト場所",4.0,4.0, 40),
            )
        )
        BottomSheetContent(earthquakeInfo)
    }
}