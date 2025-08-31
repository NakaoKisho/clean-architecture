package com.vegcale.architecture.feature.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.vegcale.architecture.R
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.ui.components.AdmobBanner
import com.vegcale.architecture.ui.theme.ArchitectureTheme
import com.vegcale.architecture.ui.theme.BoldAlpha
import com.vegcale.architecture.ui.theme.DefaultAlpha
import com.vegcale.architecture.util.BitmapHelper
import com.vegcale.architecture.util.DefaultMapUiSettings
import com.vegcale.architecture.util.rememberEmpCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    searchViewModel: SearchScreenViewModel = hiltViewModel()
) {
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        refresh = searchViewModel::refresh,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    uiState: SearchScreenUiState = SearchScreenUiState.Loading,
    refresh: () -> Unit = {},
) {
    var isSheetExpanded by rememberSaveable { mutableStateOf(false) }
    val sheetPeekHeight by animateDpAsState(
        targetValue =
            if (isSheetExpanded) {
                BottomSheetDefaults.SheetPeekHeight
            } else {
                0.dp
            },
        label = "Sheet peek height animation for BottomSheetScaffold in SearchScreen.kt"
    )

    var isLoading by rememberSaveable { mutableStateOf(false) }
    val earthquakeInfo = remember { mutableListOf<EarthquakeInfo>() }
    when (uiState) {
        is SearchScreenUiState.Failed -> {
            isLoading = false
        }
        is SearchScreenUiState.Loading -> {
            isLoading = true
        }
        is SearchScreenUiState.Success -> {
            earthquakeInfo.clear()
            earthquakeInfo.addAll(uiState.data)
            isLoading = false
        }
    }

    var clickedEarthquakeInfo by remember { mutableStateOf<EarthquakeInfo?>(null) }

    BottomSheetScaffold(
        sheetContent = { BottomSheetContent(clickedEarthquakeInfo) },
        sheetPeekHeight = sheetPeekHeight,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.primary
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            AdmobBanner()

            Box(
                modifier = Modifier.weight(weight = 1f)
            ) {
                // Button to update data
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f),
                    contentAlignment = Alignment.TopStart
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            if (!isLoading) {
                                isLoading = true
                                refresh()
                            }
                        },
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                top = 8.dp
                            ),
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            focusedElevation = 0.dp,
                            hoveredElevation = 0.dp,
                        )
                    ) {
                        val iconId = if (isLoading) R.drawable.baseline_loading else R.drawable.baseline_cached_24
                        val degrees by rememberInfiniteTransition().animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 10000),
                                repeatMode = RepeatMode.Restart
                            )
                        )
                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = null,
                            modifier = Modifier.rotate(degrees = if (isLoading) degrees else 0f),
                            tint = colorResource(R.color.blue_R000_G098_B160),
                        )
                    }
                }

                // Google Map
                var zoomLevel by rememberSaveable { mutableFloatStateOf(0f) }
                val cameraPositionState = rememberEmpCameraPositionState {
                    this.position = CameraPosition(
                        LatLng(35.6812, 139.7671),
                        zoomLevel,
                        0f,
                        0f
                    )
                }
                val googleMapUiSettings = DefaultMapUiSettings
                val scope = rememberCoroutineScope()
                val lazyListState = rememberLazyListState()
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = googleMapUiSettings,
                    onMapClick = {
                        isSheetExpanded = false
                        clickedEarthquakeInfo = null
                    }
                ) {
                    if (earthquakeInfo.isEmpty()) return@GoogleMap

                    earthquakeInfo.forEachIndexed { index, hypocenter ->
                        val markerState = rememberMarkerState(
                            position = LatLng(
                                hypocenter.latitude,
                                hypocenter.longitude
                            )
                        )
                        Marker(
                            state = markerState,
                            title = hypocenter.place,
                            snippet = hypocenter.place,
                            onClick = { _ ->
                                isSheetExpanded = true
                                clickedEarthquakeInfo = EarthquakeInfo(
                                    datetime = hypocenter.datetime,
                                    place = hypocenter.place,
                                    latitude = hypocenter.latitude,
                                    longitude = hypocenter.longitude,
                                    magnitude = hypocenter.magnitude,
                                    depth = hypocenter.depth,
                                    points = hypocenter.points
                                )
                                scope.launch {
                                    lazyListState.animateScrollToItem(index)
                                }
                                false
                            }
                        )
                    }

                    // Markers for epicenters
                    clickedEarthquakeInfo?.points?.forEach { point ->
                        if (point.latitude == null || point.longitude == null) return@forEach

                        val markerState = rememberMarkerState(
                            position = LatLng(
                                point.latitude,
                                point.longitude
                            )
                        )
                        Marker(
                            state = markerState,
                            icon = BitmapHelper().vectorToBitmap(point.scaleIcon),
                            title = point.place,
                            snippet = stringResource(point.scale)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = sheetPeekHeight),
                    contentAlignment = Alignment.BottomStart
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        state = lazyListState
                    ) {
                        items(earthquakeInfo.size) {
                            val data = earthquakeInfo[it]
                            ElevatedCard(
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(200.dp)
                                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                                    .clickable {
                                        isSheetExpanded = true
                                        scope.launch {
                                            lazyListState.animateScrollToItem(it)
                                        }
                                    }
                            ) {
                                SearchScreenCard(
                                    modifier = Modifier.weight(1f),
                                    painter = painterResource(R.drawable.baseline_access_time_24),
                                    contentDescription = stringResource(R.string.date_and_time),
                                    titleText = stringResource(R.string.date_and_time),
                                    detailText = data.datetime,
                                )
                                SearchScreenCard(
                                    modifier = Modifier.weight(1f),
                                    painter = painterResource(R.drawable.baseline_location_on_24),
                                    contentDescription = stringResource(R.string.epicenter),
                                    titleText = stringResource(R.string.epicenter),
                                    detailText = data.place,
                                )
                                SearchScreenCard(
                                    modifier = Modifier.weight(1f),
                                    painter = painterResource(R.drawable.baseline_landslide_24),
                                    contentDescription = stringResource(R.string.magnitude),
                                    titleText = stringResource(R.string.magnitude),
                                    detailText = data.magnitude.toString(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomSheetContent(itemDetail: EarthquakeInfo?) {
    if (itemDetail == null) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_access_time_24),
            contentDescription = stringResource(R.string.date_and_time),
            titleText = stringResource(R.string.date_and_time),
            detailText = itemDetail.datetime,
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_location_on_24),
            contentDescription = stringResource(R.string.epicenter),
            titleText = stringResource(R.string.epicenter),
            detailText = itemDetail.place,
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_landslide_24),
            contentDescription = stringResource(R.string.magnitude),
            titleText = stringResource(R.string.magnitude),
            detailText = itemDetail.magnitude.toString(),
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_solar_power_24),
            contentDescription = stringResource(R.string.depth),
            titleText = stringResource(R.string.depth),
            detailText = itemDetail.depth.toString(),
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_language_24),
            contentDescription = stringResource(R.string.latitude),
            titleText = stringResource(R.string.latitude),
            detailText = itemDetail.latitude.toString(),
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_language_24),
            contentDescription = stringResource(R.string.longitude),
            titleText = stringResource(R.string.longitude),
            detailText = itemDetail.longitude.toString(),
        )

        if (itemDetail.points.isEmpty()) return

        var isExpanded by rememberSaveable { mutableStateOf(false) }
        SearchScreenCard(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded },
            painter = painterResource(R.drawable.baseline_add_location_alt_24),
            contentDescription = stringResource(R.string.observation_places_info),
            titleText = stringResource(R.string.observation_places_info),
            detailText = "",
            trailingIcon = {
                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    label = "Rotation animation for isExpanded"
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_expand_more_24),
                    contentDescription = stringResource(R.string.expand_more_and_less),
                    modifier = Modifier.rotate(rotation)
                )
            }
        )

        for (point in itemDetail.points) {
            if (
                point.place == null &&
                point.latitude == null &&
                point.longitude == null
            ) return

            val sizeInPx = with(LocalDensity.current) { -40.dp.roundToPx() }
            val fadeInInitialAlpha = 0.3f
            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically {
                    sizeInPx
                } + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    initialAlpha = fadeInInitialAlpha
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Column {
                    var isDetailExpanded by rememberSaveable { mutableStateOf(false) }
                    SearchScreenCard(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(start = 24.dp)
                            .clickable { isDetailExpanded = !isDetailExpanded },
                        painter = painterResource(R.drawable.baseline_location_on_24),
                        contentDescription = stringResource(R.string.observation_place_name),
                        titleText = stringResource(R.string.observation_place_name),
                        detailText = point.place ?: stringResource(R.string.no_data),
                        trailingIcon = {
                            val rotation by animateFloatAsState(
                                targetValue = if (isDetailExpanded) 180f else 0f,
                                label = "Rotation animation for isDetailExpanded"
                            )
                            Icon(
                                painter = painterResource(R.drawable.baseline_expand_more_24),
                                contentDescription = stringResource(R.string.expand_more_and_less),
                                modifier = Modifier.rotate(rotation)
                            )
                        }
                    )

                    AnimatedVisibility(
                        visible = isDetailExpanded,
                        enter = slideInVertically {
                            sizeInPx
                        } + expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            initialAlpha = fadeInInitialAlpha
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        Column {
                            SearchScreenCard(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                painter = painterResource(R.drawable.baseline_landslide_24),
                                contentDescription = stringResource(R.string.seismic_intensity),
                                titleText = stringResource(R.string.seismic_intensity),
                                detailText = stringResource(point.scale),
                            )
                            SearchScreenCard(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                painter = painterResource(R.drawable.baseline_language_24),
                                contentDescription = stringResource(R.string.latitude),
                                titleText = stringResource(R.string.latitude),
                                detailText =
                                    if (point.latitude == null)
                                        stringResource(R.string.no_data)
                                    else
                                        point.latitude.toString(),
                            )
                            SearchScreenCard(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                painter = painterResource(R.drawable.baseline_language_24),
                                contentDescription = stringResource(R.string.longitude),
                                titleText = stringResource(R.string.longitude),
                                detailText =
                                    if (point.longitude == null)
                                        stringResource(R.string.no_data)
                                    else
                                        point.longitude.toString(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreenCard(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String,
    titleText: String,
    detailText: String,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier
                    .weight(0.3f)
                    .alpha(DefaultAlpha)
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(2.dp)
                    .padding(vertical = 10.dp)
                    .heightIn(min = 30.dp, max = 50.dp)
                    .alpha(DefaultAlpha)
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(if (trailingIcon == null) 1f else 0.9f)
            ) {
                Text(
                    text = titleText,
                    modifier = Modifier.alpha(BoldAlpha),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = detailText,
                    modifier = Modifier.alpha(DefaultAlpha),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }
            if (trailingIcon != null) {
                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .alpha(DefaultAlpha),
                    contentAlignment = Alignment.Center
                ) {
                    trailingIcon()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    ArchitectureTheme {
        SearchScreen()
    }
}
