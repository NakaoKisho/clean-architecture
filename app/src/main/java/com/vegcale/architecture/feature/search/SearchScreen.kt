package com.vegcale.architecture.feature.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.vegcale.architecture.R
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.Points
import com.vegcale.architecture.ui.theme.ArchitectureTheme
import com.vegcale.architecture.ui.theme.BoldAlpha
import com.vegcale.architecture.ui.theme.DefaultAlpha

@Composable
fun SearchScreen() {
    val earthquakeInfo = listOf(
        EarthquakeInfo(
            datetime = "2023/01/01 01:00",
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
        ),
        EarthquakeInfo(
            datetime = "2023/01/02 01:00",
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
        ),
        EarthquakeInfo(
            datetime = "2023/01/03 01:00",
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
        ),
        EarthquakeInfo(
            datetime = "2023/01/04 01:00",
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
        ),
        EarthquakeInfo(
            datetime = "2023/01/05 01:00",
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
        ),
    )

    SearchScreen(Modifier, earthquakeInfo)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    earthquakeInfo: List<EarthquakeInfo>,
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
    BottomSheetScaffold(
        sheetContent = { BottomSheetContent() },
        modifier = modifier,
        sheetPeekHeight = sheetPeekHeight,
        sheetDragHandle = { BottomSheetDefaults.DragHandle(
            color = MaterialTheme.colorScheme.primary
        ) },
        topBar = {
            SearchTopAppBar(
                listOnClick = {},
                searchOnClick = {}
            )
        }
    ) {
        val cameraPositionState = rememberCameraPositionState {
            this.position = CameraPosition(
                LatLng(0.0, 0.0),
                0f,
                0f,
                0f
            )
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { isSheetExpanded = false }
        ) {
            Marker()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = sheetPeekHeight),
            contentAlignment = Alignment.BottomStart
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(earthquakeInfo.size) {
                    val data = earthquakeInfo[it]
                    Box(
                        modifier = Modifier
                            .clickable { isSheetExpanded = true }
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .width(220.dp)
                                .height(200.dp)
                                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
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

@Composable
private fun BottomSheetContent() {
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
            detailText = "data.datetime",
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_location_on_24),
            contentDescription = stringResource(R.string.epicenter),
            titleText = stringResource(R.string.epicenter),
            detailText = "data.place",
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_landslide_24),
            contentDescription = stringResource(R.string.magnitude),
            titleText = stringResource(R.string.magnitude),
            detailText = "data.magnitude.toString()",
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_solar_power_24),
            contentDescription = stringResource(R.string.depth),
            titleText = stringResource(R.string.depth),
            detailText = "data.depth",
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_language_24),
            contentDescription = stringResource(R.string.latitude),
            titleText = stringResource(R.string.latitude),
            detailText = "data.latitude",
        )
        SearchScreenCard(
            painter = painterResource(R.drawable.baseline_language_24),
            contentDescription = stringResource(R.string.longitude),
            titleText = stringResource(R.string.longitude),
            detailText = "data.longitude",
        )

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
        )  {
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
                    detailText = "data.observation_place_namedata.observation_place_namedata.observation_place_namedata.observation_place_namedata.observation_place_namedata.observation_place_name",
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
                            detailText = "data.seismic_intensity",
                        )
                        SearchScreenCard(
                            modifier = Modifier
                                .padding(start = 24.dp),
                            painter = painterResource(R.drawable.baseline_language_24),
                            contentDescription = stringResource(R.string.latitude),
                            titleText = stringResource(R.string.latitude),
                            detailText = "data.latitude",
                        )
                        SearchScreenCard(
                            modifier = Modifier
                                .padding(start = 24.dp),
                            painter = painterResource(R.drawable.baseline_language_24),
                            contentDescription = stringResource(R.string.longitude),
                            titleText = stringResource(R.string.longitude),
                            detailText = "data.longitude",
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopAppBar(
    modifier: Modifier = Modifier,
    listOnClick: () -> Unit,
    searchOnClick: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {},
        modifier = modifier,
        actions = {
            TextField(
                value = "",
                onValueChange = {},
                trailingIcon = {
                    IconButton(onClick = searchOnClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search_icon_description),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                shape = RoundedCornerShape(5.dp)
            )

            var isDescendingOrderClicked by rememberSaveable { mutableStateOf(true) }
            var isAscendingOrderClicked by rememberSaveable { mutableStateOf(false) }
            var isCurrentLocationOrderClicked by rememberSaveable { mutableStateOf(false) }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = stringResource(R.string.list_icon_description),
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                        isDescendingOrderClicked =
                            (!isDescendingOrderClicked &&
                                    !isAscendingOrderClicked &&
                                    !isCurrentLocationOrderClicked)
                    }
                ) {
                    val dropdownMenuItems = arrayOf(
                        SearchDropdownMenuItemProperties(
                            text = stringResource(R.string.ascending_order),
                            onClick = {
                                isDescendingOrderClicked =
                                    if (!isDescendingOrderClicked)  {
                                        if (isAscendingOrderClicked || isCurrentLocationOrderClicked) {
                                            isAscendingOrderClicked = false
                                            isCurrentLocationOrderClicked = false
                                        }
                                        true
                                    } else {
                                        false
                                    }
                            },
                            modifier = Modifier.alpha(if (isDescendingOrderClicked) 1f else 0.6f)
                        ),
                        SearchDropdownMenuItemProperties(
                            text = stringResource(R.string.descending_order),
                            onClick = {
                                isAscendingOrderClicked =
                                    if (!isAscendingOrderClicked)  {
                                        if (isDescendingOrderClicked || isCurrentLocationOrderClicked) {
                                            isDescendingOrderClicked = false
                                            isCurrentLocationOrderClicked = false
                                        }
                                        true
                                    } else {
                                        false
                                    }
                            },
                            modifier = Modifier.alpha(if (isAscendingOrderClicked) 1f else 0.6f)
                        ),
                        SearchDropdownMenuItemProperties(
                            text = stringResource(R.string.closeness_order),
                            onClick = {
                                isCurrentLocationOrderClicked =
                                    if (!isCurrentLocationOrderClicked)  {
                                        if (isDescendingOrderClicked || isAscendingOrderClicked) {
                                            isDescendingOrderClicked = false
                                            isAscendingOrderClicked = false
                                        }
                                        true
                                    } else {
                                        false
                                    }
                            },
                            modifier = Modifier.alpha(if (isCurrentLocationOrderClicked) 1f else 0.6f)
                        )
                    )

                    dropdownMenuItems.map {
                        DropdownMenuItem(
                            text = { Text(it.text) },
                            onClick = it.onClick,
                            modifier = it.modifier
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

data class SearchDropdownMenuItemProperties(
    val text: String,
    val onClick: () -> Unit,
    val modifier: Modifier = Modifier
)

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
            Divider(
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
    val earthquakeInfo = listOf(
        EarthquakeInfo(
            datetime = "2023/01/01 01:00",
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
        ),
        EarthquakeInfo(
            datetime = "2023/01/02 01:00",
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
        ),
        EarthquakeInfo(
            datetime = "2023/01/03 01:00",
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
        ),
        EarthquakeInfo(
            datetime = "2023/01/04 01:00",
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
        ),
        EarthquakeInfo(
            datetime = "2023/01/05 01:00",
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
        ),
    )

    ArchitectureTheme {
        SearchScreen(Modifier, earthquakeInfo)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    BottomSheetContent()
}