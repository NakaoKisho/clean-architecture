package com.vegcale.architecture.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vegcale.architecture.data.P2pquakeRepository
import com.vegcale.architecture.data.UsgsEarthquakeRepository
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.di.DefaultDispatcher
import com.vegcale.architecture.ui.MainActivityUiState.Success
import com.vegcale.architecture.usecase.GetLatestEarthquakeInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import okio.IOException
import javax.inject.Inject

/**
 * @HiltViewModel でアノテーションをすることで、Hiltにインスタンス化の方法を
 * @Inject でアノテーションをすることで、Hiltにクラスのインスタンス化する方法を提供します。
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    usgsEarthquakeRepository: UsgsEarthquakeRepository,
    p2pquakeRepository: P2pquakeRepository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> =
        try {
            GetLatestEarthquakeInfoUseCase(
                usgsEarthquakeRepository,
                p2pquakeRepository,
            )
                .invoke()
                .flowOn(defaultDispatcher)
                .map<List<EarthquakeInfo>, MainActivityUiState>(::Success)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = MainActivityUiState.Loading,
                )
        } catch (e: IOException) {
            MutableStateFlow(MainActivityUiState.Error).asStateFlow()
        }
}

sealed interface MainActivityUiState {
    object Loading: MainActivityUiState
    data class Success(val earthquakeData: List<EarthquakeInfo>): MainActivityUiState
    object Error: MainActivityUiState
}