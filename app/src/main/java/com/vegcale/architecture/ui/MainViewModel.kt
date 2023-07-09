package com.vegcale.architecture.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vegcale.architecture.data.EarthquakeRepository
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.ui.MainActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * @HiltViewModel でアノテーションをすることで、Hiltにインスタンス化の方法を
 * @Inject でアノテーションをすることで、Hiltにクラスのインスタンス化する方法を提供します。
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    earthquakeRepository: EarthquakeRepository
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = earthquakeRepository
        .getInfo("geojson", 10, "time")
        .map<EarthquakeInfo, MainActivityUiState>(::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainActivityUiState.Loading,
        )
}

sealed interface MainActivityUiState {
    object Loading: MainActivityUiState
    data class Success(val earthquakeData: EarthquakeInfo): MainActivityUiState
}