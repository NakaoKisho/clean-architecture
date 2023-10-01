package com.vegcale.architecture.feature.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vegcale.architecture.data.OfflineUserDataRepository
import com.vegcale.architecture.data.model.SeismicIntensity2
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class SettingsScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val offlineUserDataRepository: OfflineUserDataRepository
): ViewModel()  {
    val uiState =
        offlineUserDataRepository
            .userData
            .map {
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        isNotificationOn = it.isNotificationOn,
                        placeIndexes = it.placeIndexes,
                        minIntensityLevelIndex = it.minIntensityLevelIndex,
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState.Loading,
            )

    // Notification state
    fun setNotification(isOn: Boolean) {
        viewModelScope.launch {
            offlineUserDataRepository.setIsNotificationOn(isOn)
        }
    }

    // Place state
    fun addSelectedPlace(index: Int) {
        viewModelScope.launch {
            offlineUserDataRepository.addPlaceIndex(index)
        }
    }
    fun removeSelectedPlace(index: Int) {
        viewModelScope.launch {
            offlineUserDataRepository.deletePlaceIndex(index)
        }
    }
    private val _placeArrayFirstIndex = 0
    fun addAllPlaces(arraySize: Int){
        viewModelScope.launch {
            val newList = mutableListOf<Int>()
            for (count in _placeArrayFirstIndex until arraySize) {
                newList.add(count)
            }

            offlineUserDataRepository.addPlaceIndex(newList)
        }
    }
    fun removeAllPlaces() {
        viewModelScope.launch {
            offlineUserDataRepository.clearPlaceIndexes()
        }
    }
    fun addItemAll() {
        viewModelScope.launch {
            offlineUserDataRepository.addPlaceIndex(_placeArrayFirstIndex)
        }
    }
    fun removeItemAll() {
        viewModelScope.launch {
            offlineUserDataRepository.deletePlaceIndex(_placeArrayFirstIndex)
        }
    }

    // Min intensity level state
    fun setSelectedMinIntensityLevelIndex(index: Int) {
        viewModelScope.launch {
            offlineUserDataRepository.setMinIntensityLevelIndex(index)
        }
    }
    private fun getPlaceNames(
        places: Array<String>,
        placeIndexes: List<Int>
    ):  MutableList<String> {
        val placeNames = mutableListOf<String>()
        for (index in placeIndexes) {
            if (index == 0) continue
            placeNames.add(places[index])
        }

        return placeNames
    }
    private fun convertToSeismicIntensity(intensityLevelIndex: Int) =
        when (intensityLevelIndex) {
            0 -> SeismicIntensity2.IntensityOfOne
            1 -> SeismicIntensity2.IntensityOfTwo
            2 -> SeismicIntensity2.IntensityOfThree
            3 -> SeismicIntensity2.IntensityOfFour
            4 -> SeismicIntensity2.IntensityOfLowerFive
            5 -> SeismicIntensity2.IntensityOfUpperFive
            6 -> SeismicIntensity2.IntensityOfLowerSix
            7 -> SeismicIntensity2.IntensityOfUpperSix
            8 -> SeismicIntensity2.IntensityOfSeven
            else -> throw Error("intensityLevel should be from 0 to 8")
        }
}

data class UserEditableSettings(
    val isNotificationOn: Boolean,
    val placeIndexes: List<Int>,
    val minIntensityLevelIndex: Int,
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}