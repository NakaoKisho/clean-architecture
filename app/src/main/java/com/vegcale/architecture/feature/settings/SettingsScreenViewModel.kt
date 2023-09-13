package com.vegcale.architecture.feature.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(): ViewModel()  {
    private var _isNotificationOn = MutableStateFlow(false)
    val isNotificationOn: StateFlow<Boolean> = _isNotificationOn
    fun setNotification(onOrOff: Boolean) {
        _isNotificationOn.value = onOrOff
    }

    private var _selectedPlaces = MutableStateFlow(emptyList<String>())
    val selectedPlaces: StateFlow<List<String>> = _selectedPlaces
    fun addSelectedPlace(item: String) {
        val newList = listOf(item)
        _selectedPlaces.value = _selectedPlaces.value + newList
    }
    fun removeSelectedPlace(item: String) {
        val newList = listOf(item)
        _selectedPlaces.value = _selectedPlaces.value - newList.toSet()
    }

    private var _selectedMinIntensityLevelIndex = MutableStateFlow(0)
    val selectedMinIntensityLevelIndex: StateFlow<Int> = _selectedMinIntensityLevelIndex
    fun setSelectedMinIntensityLevelIndex(index: Int) {
        _selectedMinIntensityLevelIndex.value = index
    }
}