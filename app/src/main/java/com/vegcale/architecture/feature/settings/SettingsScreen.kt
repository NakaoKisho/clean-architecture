package com.vegcale.architecture.feature.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vegcale.architecture.R
import com.vegcale.architecture.ui.components.AdmobBanner
import com.vegcale.architecture.ui.theme.BoldAlpha
import com.vegcale.architecture.ui.theme.DefaultAlpha
import com.vegcale.architecture.util.isPermissionGranted
import kotlinx.coroutines.launch

private const val TAG = "Settings Screen"

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    when (settingsUiState) {
        is SettingsUiState.Loading -> {
            LoadingTile()
        }
        is SettingsUiState.Success -> {
            SettingsScreen(
                addAllPlaces = settingsViewModel::addAllPlaces,
                addItemAll = settingsViewModel::addItemAll,
                addPlace = settingsViewModel::addPlace,
                clearPlaces = settingsViewModel::clearPlaces,
                deleteItemAll = settingsViewModel::deleteItemAll,
                deletePlace = settingsViewModel::deletePlace,
                notificationState = (settingsUiState as SettingsUiState.Success).settings.isNotificationOn,
                selectedMinIntensityLevelIndexState = (settingsUiState as SettingsUiState.Success).settings.minIntensityLevelIndex,
                selectedPlacesState = (settingsUiState as SettingsUiState.Success).settings.places,
                setNotification = settingsViewModel::setNotification,
                setSelectedMinIntensityLevelIndex = settingsViewModel::setSelectedMinIntensityLevelIndex,
            )
        }
    }
}

@Composable
internal fun SettingsScreen(
    addAllPlaces: (Array<String>) -> Unit,
    addItemAll: (String) -> Unit,
    addPlace: (String) -> Unit,
    clearPlaces: () -> Unit,
    deleteItemAll: (String) -> Unit,
    deletePlace: (String) -> Unit,
    notificationState: Boolean,
    selectedMinIntensityLevelIndexState: Int,
    selectedPlacesState: List<String>,
    setNotification: (Boolean) -> Unit,
    setSelectedMinIntensityLevelIndex: (Int) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        // Permission
        val context = LocalContext.current
        var shouldAskPermission by remember { mutableStateOf(true) }
        val deniedNotificationMessage = stringResource(R.string.denied_notification_permission)
        val goToSettingsMessage = stringResource(R.string.go_to_settings)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val coroutineScope = rememberCoroutineScope()
            val requestNotificationPermission =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissionsMap ->
                    if (permissionsMap.all { !it.value }) {
                        setNotification(false)
                        coroutineScope.launch {
                            val snackbarResult = snackbarHostState.showSnackbar(
                                message = deniedNotificationMessage,
                                actionLabel = goToSettingsMessage
                            )
                            if (snackbarResult == SnackbarResult.ActionPerformed) {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", context.packageName, null)
                                )
                                if (intent.resolveActivity(context.packageManager) != null) {
                                    ContextCompat.startActivity(context, intent, null)
                                } else {
                                    Log.e(TAG, "No component can handle this intent")
                                }
                            }
                        }
                    } else {
                        shouldAskPermission = false
                    }
                }

            val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            val isPermissionsGranted = permissions.all { permission ->
                isPermissionGranted(context, permission)
            }
            if (notificationState && shouldAskPermission) {
                if (isPermissionsGranted) {
                    SideEffect {
                        requestNotificationPermission.launch(permissions)
                    }
                } else {
                    PermissionExplanationDialog(
                        confirmButtonText = stringResource(R.string.yes),
                        dismissButtonText = stringResource(R.string.no),
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.baseline_notifications_24
                                ),
                                contentDescription = stringResource(
                                    R.string.notification_permission_icon_content_description
                                )
                            )
                        },
                        onConfirm = {
                            requestNotificationPermission.launch(permissions)
                        },
                        onDismiss = {
                            setNotification(false)
                        },
                        text = stringResource(R.string.permission_explanation_text),
                        title = stringResource(R.string.permission_explanation_title),
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Admob
            AdmobBanner()

            // Notification
            val intensityLevels = stringArrayResource(R.array.intensity_levels)
            SettingScreenItemTile(
                modifier = Modifier
                    .clickable {
                        setNotification(!notificationState)
                    },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_notifications_24),
                        contentDescription = stringResource(
                            R.string.notification_icon_content_description
                        ),
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
                        text = stringResource(
                            R.string.notification_text
                        ),
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
                    // Description
                    Text(
                        text = stringResource(
                            R.string.notification_description_text
                        ),
                        modifier = Modifier.padding(
                            start = 13.dp,
                            top = 4.dp,
                            end = 13.dp,
                            bottom = 0.dp
                        )
                    )

                    // Error Message
                    if (selectedPlacesState.isEmpty()) {
                        Text(
                            text = stringResource(
                                R.string.notification_description_error_text
                            ),
                            modifier = Modifier.padding(
                                start = 13.dp,
                                top = 0.dp,
                                end = 13.dp,
                                bottom = 0.dp
                            ),
                            color = Color.Red
                        )
                    }

                    // Prefectures
                    SettingScreenItemTile(
                        modifier = Modifier.height(400.dp)
                    ) {
                        Column {
                            Text(
                                text = stringResource(
                                    R.string.place_select_text
                                )
                            )

                            val places = stringArrayResource(R.array.places_of_country)
                            LazyColumn(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val firstIndex = 0
                                items(places.size) {index ->
                                    SettingScreenItemTile(
                                        modifier = Modifier
                                            .clickable {
                                                if (index == firstIndex) {
                                                    if (selectedPlacesState.contains(places[firstIndex])) {
                                                        clearPlaces()
                                                    } else {
                                                        addAllPlaces(places)
                                                    }
                                                } else {
                                                    if (selectedPlacesState.contains(places[index])) {
                                                        deletePlace(places[index])
                                                        if (selectedPlacesState.contains(places[firstIndex])) {
                                                            deleteItemAll(places[firstIndex])
                                                        }
                                                    } else {
                                                        addPlace(places[index])
                                                        if ((selectedPlacesState.size + 1) == (places.size - 1)) {
                                                            addItemAll(places[firstIndex])
                                                        }
                                                    }
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
                                text = stringResource(
                                    R.string.min_intensity_level_select_text
                                )
                            )

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
}

// Permission dialog
@Composable
private fun PermissionExplanationDialog(
    confirmButtonText: String,
    dismissButtonText: String,
    icon: @Composable (() -> Unit)?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    text: String,
    title: String,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        icon = icon,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissButtonText)
            }
        }
    )
}

@Composable
private fun LoadingTile(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "rememberInfiniteTransition in LoadingTile in Settings Screen")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "animateFloat in LoadingTile in Settings Screen"
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_loading),
            contentDescription = "",
            modifier = Modifier
                .padding(end = 8.dp)
                .rotate(rotation),
            tint = colorResource(R.color.blue_R000_G098_B160)
        )
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
                VerticalDivider(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .heightIn(min = 30.dp, max = 50.dp)
                        .alpha(DefaultAlpha),
                    thickness = 2.dp,
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