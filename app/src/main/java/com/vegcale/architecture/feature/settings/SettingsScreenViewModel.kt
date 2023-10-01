package com.vegcale.architecture.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vegcale.architecture.data.OfflineUserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val offlineUserDataRepository: OfflineUserDataRepository
): ViewModel()  {
    val uiState =
        offlineUserDataRepository
            .userData
            .map {
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        isNotificationOn = it.isNotificationOn,
                        places = it.places,
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
    fun addPlace(place: String) {
        viewModelScope.launch {
            offlineUserDataRepository.addPlace(place)
        }
    }
    fun deletePlace(place: String) {
        viewModelScope.launch {
            offlineUserDataRepository.deletePlace(place)
        }
    }
    fun addAllPlaces(places: Array<String>){
        viewModelScope.launch {
            offlineUserDataRepository.addPlaces(places.toList())
        }
    }
    fun clearPlaces() {
        viewModelScope.launch {
            offlineUserDataRepository.clearPlaces()
        }
    }
    fun addItemAll(itemAll: String) {
        viewModelScope.launch {
            offlineUserDataRepository.addPlace(itemAll)
        }
    }
    fun deleteItemAll(itemAll: String) {
        viewModelScope.launch {
            offlineUserDataRepository.deletePlace(itemAll)
        }
    }

    // Min intensity level state
    fun setSelectedMinIntensityLevelIndex(index: Int) {
        viewModelScope.launch {
            offlineUserDataRepository.setMinIntensityLevelIndex(index)
        }
    }
}

data class UserEditableSettings(
    val isNotificationOn: Boolean,
    val places: List<String>,
    val minIntensityLevelIndex: Int,
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}