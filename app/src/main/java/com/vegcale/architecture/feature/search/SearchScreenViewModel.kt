package com.vegcale.architecture.feature.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.vegcale.architecture.R
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.data.model.Points
import com.vegcale.architecture.data.model.SeismicIntensity
import com.vegcale.architecture.usecase.GetLatestEarthquakeInfoUseCase2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getLatestEarthquakeInfoUseCase: GetLatestEarthquakeInfoUseCase2
) : ViewModel() {
    private val earthquakeData = mutableListOf<EarthquakeInfo>()
    private val tokyoLatLng = LatLng(35.6812, 139.7671)
    var uiState by mutableStateOf(
        UiState(
            earthquakeData = getLatestEarthquakeInfoUseCase()
                .filter {
                    it.place != "" && it.magnitude > 0
                }
                .map<EarthquakeInfo, SearchScreenUiState> {
                    earthquakeData.add(it)
                    SearchScreenUiState.Success(data = earthquakeData)
                }
                .catch { emit(SearchScreenUiState.Error) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = SearchScreenUiState.Loading,
                ),
            hasLocationAccess = false,
            latLng = tokyoLatLng,
            clickedEarthquakeInfo = null,
        )
    )

    fun updateEarthquakeInfo(
        datetime: String,
        place: String,
        latitude: Double,
        longitude: Double,
        magnitude: Double,
        depth: Int,
        points: List<Points>,
        substituteText: String,
    ) {
        viewModelScope.launch {
            val newState = uiState.copy(
                latLng = LatLng(latitude, longitude),
                clickedEarthquakeInfo = ClickedEarthquakeInfo(
                    datetime = datetime,
                    place = place,
                    latitude = latitude.toString(),
                    longitude = longitude.toString(),
                    magnitude = magnitude.toString(),
                    depth = depth.toString(),
                    points = points
                        .map { point ->
                            PointsForDisplay(
                                place = point.place ?: substituteText,
                                latitude =
                                    if (point.latitude == null) substituteText
                                    else point.latitude.toString(),
                                longitude =
                                    if (point.longitude == null) substituteText
                                    else point.longitude.toString(),
                                scaleStringId =
                                    if (point.scale == null)
                                        convertScaleToStringResourceId(SeismicIntensity.NoData)
                                    else convertScaleToStringResourceId(point.scale),
                                scaleDrawableId =
                                    if (point.scale == null)
                                        convertScaleToDrawableResourceId(SeismicIntensity.NoData)
                                    else convertScaleToDrawableResourceId(point.scale),
                            )
                        }
                )
            )
            uiState = newState
        }
    }

    private suspend fun convertScaleToStringResourceId(seismicIntensity: Byte): Int {
        return withContext(Dispatchers.IO) {
            when (seismicIntensity) {
                SeismicIntensity.NoData -> R.string.no_data
                SeismicIntensity.IntensityOfOne -> R.string.seismic_intensity_of_one
                SeismicIntensity.IntensityOfTwo -> R.string.seismic_intensity_of_two
                SeismicIntensity.IntensityOfThree -> R.string.seismic_intensity_of_three
                SeismicIntensity.IntensityOfFour -> R.string.seismic_intensity_of_four
                SeismicIntensity.IntensityOfLowerFive -> R.string.seismic_intensity_of_lower_five
                SeismicIntensity.IntensityOfMoreThanUpperFive -> R.string.seismic_intensity_of_more_than_upper_five
                SeismicIntensity.IntensityOfUpperFive -> R.string.seismic_intensity_of_upper_five
                SeismicIntensity.IntensityOfLowerSix -> R.string.seismic_intensity_of_lower_six
                SeismicIntensity.IntensityOfUpperSix -> R.string.seismic_intensity_of_upper_six
                SeismicIntensity.IntensityOfSeven -> R.string.seismic_intensity_of_seven
                else -> R.string.no_data
            }
        }
    }

    private suspend fun convertScaleToDrawableResourceId(stringResourceId: Byte): Int {
        return withContext(Dispatchers.IO) {
            when (stringResourceId) {
                SeismicIntensity.NoData -> R.drawable.baseline_question_mark_24
                SeismicIntensity.IntensityOfOne -> R.drawable.seismic_intensity_of_one
                SeismicIntensity.IntensityOfTwo -> R.drawable.seismic_intensity_of_two
                SeismicIntensity.IntensityOfThree -> R.drawable.seismic_intensity_of_three
                SeismicIntensity.IntensityOfFour -> R.drawable.seismic_intensity_of_four
                SeismicIntensity.IntensityOfLowerFive -> R.drawable.seismic_intensity_of_lower_five
                SeismicIntensity.IntensityOfMoreThanUpperFive -> R.drawable.seismic_intensity_of_more_than_upper_five
                SeismicIntensity.IntensityOfUpperFive -> R.drawable.seismic_intensity_of_upper_five
                SeismicIntensity.IntensityOfLowerSix -> R.drawable.seismic_intensity_of_lower_six
                SeismicIntensity.IntensityOfUpperSix -> R.drawable.seismic_intensity_of_upper_six
                SeismicIntensity.IntensityOfSeven -> R.drawable.seismic_intensity_of_seven
                else -> R.drawable.baseline_question_mark_24
            }
        }
    }

    fun resetEarthquakeInfo() {
        viewModelScope.launch {
            val newState = uiState.copy(
                clickedEarthquakeInfo = null
            )
            uiState = newState
        }
    }

    fun updateData() {
        viewModelScope.launch {
            val newEarthquakeData = getLatestEarthquakeInfoUseCase()
                .filter {
                    it.place != "" && it.magnitude > 0
                }
                .map<EarthquakeInfo, SearchScreenUiState> {
                    earthquakeData.add(it)
                    SearchScreenUiState.Success(data = earthquakeData)
                }
                .catch { emit(SearchScreenUiState.Error) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = SearchScreenUiState.Loading,
                )

            val newState = uiState.copy(earthquakeData = newEarthquakeData)
            uiState = newState
        }
    }

    data class PointsForDisplay(
        val place: String,
        val latitude: String,
        val longitude: String,
        val scaleStringId: Int,
        val scaleDrawableId: Int,
    )
    data class ClickedEarthquakeInfo(
        val datetime: String,
        val place: String,
        val latitude: String,
        val longitude: String,
        val magnitude: String,
        val depth: String,
        val points: List<PointsForDisplay>,
    )
    data class UiState(
        val earthquakeData: StateFlow<SearchScreenUiState>,
        val hasLocationAccess: Boolean,
        val latLng: LatLng,
        var clickedEarthquakeInfo: ClickedEarthquakeInfo?
    )
}

sealed interface SearchScreenUiState {
    object Loading: SearchScreenUiState
    data class Success(val data: List<EarthquakeInfo>): SearchScreenUiState
    object Error: SearchScreenUiState
}