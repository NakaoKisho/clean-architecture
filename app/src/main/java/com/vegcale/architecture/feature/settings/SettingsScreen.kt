package com.vegcale.architecture.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vegcale.architecture.R
import com.vegcale.architecture.ui.theme.BoldAlpha
import com.vegcale.architecture.ui.theme.DefaultAlpha

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val notificationState by settingsViewModel.isNotificationOn.collectAsStateWithLifecycle()
    val selectedPlacesState by settingsViewModel.selectedPlaces.collectAsStateWithLifecycle()
    val selectedMinIntensityLevelIndexState by settingsViewModel.selectedMinIntensityLevelIndex.collectAsStateWithLifecycle()
    SettingsScreen(
        notificationState = notificationState,
        setNotification = settingsViewModel::setNotification,
        selectedPlacesState = selectedPlacesState,
        addSelectedPlace = settingsViewModel::addSelectedPlace,
        removeSelectedPlace = settingsViewModel::removeSelectedPlace,
        selectedMinIntensityLevelIndexState = selectedMinIntensityLevelIndexState,
        setSelectedMinIntensityLevelIndex = settingsViewModel::setSelectedMinIntensityLevelIndex
    )
}

@Composable
internal fun SettingsScreen(
    notificationState: Boolean,
    setNotification: (Boolean) -> Unit,
    selectedPlacesState: List<String>,
    addSelectedPlace: (String) -> Unit,
    removeSelectedPlace: (String) -> Unit,
    selectedMinIntensityLevelIndexState: Int,
    setSelectedMinIntensityLevelIndex: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Notification
        SettingScreenItemTile(
            modifier = Modifier.clickable { setNotification(!notificationState) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_notifications_24),
                    contentDescription = "",
                    modifier = Modifier
                        .weight(0.3f)
                        .alpha(DefaultAlpha)
                )
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "通知設定",
                    modifier = Modifier
                        .alpha(BoldAlpha)
                        .weight(0.7f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .weight(0.3f)
                        .alpha(DefaultAlpha),
                    contentAlignment = Alignment.Center
                ) {
                    Switch(
                        checked = notificationState,
                        onCheckedChange = null,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = colorResource(R.color.blue_R000_G098_B160)
                        )
                    )
                }
            }
        }

        // Notification style
        val sizeInPx = with(LocalDensity.current) { -40.dp.roundToPx() }
        val fadeInInitialAlpha = 0.3f
        AnimatedVisibility(
            visible = notificationState,
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
                // Prefectures
                SettingScreenItemTile(
                    modifier = Modifier.height(400.dp)
                ) {
                    Column {
                        Text(
                            text = "都道府県を選択してください(複数選択可)\n選択した都道府県で地震が発生した場合は通知します selected items: $selectedPlacesState"
                        )

                        val places = stringArrayResource(R.array.places_of_country)
                        LazyColumn(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(places.size) {index ->
                                SettingScreenItemTile(
                                    modifier = Modifier
                                        .clickable {
                                            if (selectedPlacesState.contains(places[index])) {
                                                removeSelectedPlace(places[index])
                                            } else {
                                                addSelectedPlace(places[index])
                                            }
                                        },
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = selectedPlacesState.contains(places[index]),
                                            onCheckedChange = null
                                        )
                                        Text(
                                            text = places[index]
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Intensity level
                SettingScreenItemTile(
                    modifier = Modifier.height(400.dp)
                ) {
                    Column {
                        Text(
                            text = "通知する最小震度を選択してください(複数選択不可)\n選択した震度以上の場合は通知します"
                        )

                        val intensityLevels = stringArrayResource(R.array.intensity_levels)
                        LazyColumn(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(intensityLevels.size) { index ->
                                SettingScreenItemTile(
                                    modifier = Modifier
                                        .clickable {
                                            setSelectedMinIntensityLevelIndex(index)
                                        }
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = selectedMinIntensityLevelIndexState == index,
                                            onCheckedChange = null
                                        )
                                        Text(
                                            text = intensityLevels[index]
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingScreenItemTile(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
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
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier.weight(0.3f),
                    contentAlignment = Alignment.Center
                ) {
                    leadingIcon()
                }
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .padding(vertical = 10.dp)
                        .heightIn(min = 30.dp, max = 50.dp)
                        .alpha(DefaultAlpha)
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}