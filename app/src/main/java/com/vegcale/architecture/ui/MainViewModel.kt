package com.vegcale.architecture.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.di.DefaultDispatcher
import com.vegcale.architecture.usecase.GetLatestEarthquakeInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    getLatestEarthquakeInfoUseCase: GetLatestEarthquakeInfoUseCase,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    data class UiState(
        val earthquakeData: StateFlow<MainActivityUiState>,
        val hasLocationAccess: Boolean,
        val latLng: LatLng
    )
    private val tokyoLatLng = LatLng(35.6812, 139.7671)
    var uiState by mutableStateOf(
        UiState(
            earthquakeData = try {
                getLatestEarthquakeInfoUseCase()
                    .flowOn(defaultDispatcher)
                    .map<List<EarthquakeInfo>, MainActivityUiState>(MainActivityUiState::Success)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5000),
                        initialValue = MainActivityUiState.Loading,
                    )
            } catch (e: IOException) {
                MutableStateFlow(MainActivityUiState.Error).asStateFlow()
            },
            hasLocationAccess = false,
            latLng =  tokyoLatLng
        )
    )

    fun onPermissionChange(permission: String, isGranted: Boolean) {
        when (permission) {
            Manifest.permission.ACCESS_COARSE_LOCATION -> {
                uiState = uiState.copy(hasLocationAccess = isGranted)
            }
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                uiState = uiState.copy(hasLocationAccess = isGranted)
            }
            else -> {
                Log.e("Permission change", "Unexpected permission: $permission")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener

            val currentLatLng = LatLng(location.latitude, location.longitude)
            uiState = uiState.copy(latLng = currentLatLng)

//            val geocoder = Geocoder(context, Locale.getDefault())
//
//            if (Build.VERSION.SDK_INT >= 33) {
//                geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
//                    val address = addresses.firstOrNull()
//                    val place = address?.locality ?: address?.subAdminArea ?: address?.adminArea
//                    ?: address?.countryName
////                    uiState = uiState.copy(place = place)
//                }
//            } else {
//                val address =
//                    geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
//                        ?: return@addOnSuccessListener
//                val place =
//                    address.locality ?: address.subAdminArea ?: address.adminArea ?: address.countryName
//                    ?: return@addOnSuccessListener
//
////                uiState = uiState.copy(place = place)
//            }
        }
    }
}

sealed interface MainActivityUiState {
    object Loading: MainActivityUiState
    data class Success(val earthquakeData: List<EarthquakeInfo>): MainActivityUiState
    object Error: MainActivityUiState
}