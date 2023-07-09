package com.vegcale.architecture.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.vegcale.architecture.data.model.EarthquakeInfoFeatures
import com.vegcale.architecture.ui.theme.ArchitectureTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val items = viewModel.uiState.collectAsStateWithLifecycle()

    if (items.value is MainActivityUiState.Success) {
        MainScreen((items.value as MainActivityUiState.Success).earthquakeData.features)
    } else {
        val noItemList = emptyList<EarthquakeInfoFeatures>()
        MainScreen(noItemList)
    }
}

@Composable
internal fun MainScreen(
    items: List<EarthquakeInfoFeatures>
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            val tokyo = LatLng(35.6812, 139.7671)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(tokyo, 10f)
            }

            GoogleMap(
                modifier = Modifier.weight(1f),
                cameraPositionState = cameraPositionState
            ) {
                items.forEach {
                    val latitude = it.geometry.coordinates[1]
                    val longitude = it.geometry.coordinates[0]
                    Marker(
                        position = LatLng(latitude, longitude),
                        title = it.properties.title,
                        snippet = it.properties.place
                    )
                }
            }

            Box (
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
            ) {
                LazyRow {
                    items(10) {
                        Card(
                            modifier = Modifier
                                .size(100.dp, 100.dp)
                                .padding(5.dp),
                            elevation = CardDefaults.elevatedCardElevation(5.dp),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = "https://flagcdn.com/w320/za.webp",
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(0.dp, 5.dp, 0.dp, 0.dp)
                                        .weight(1.5f),
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .weight(1f),
                                    text = "Singapore",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ArchitectureTheme {
        val noItemList = emptyList<EarthquakeInfoFeatures>()
        MainScreen(noItemList)
    }
}