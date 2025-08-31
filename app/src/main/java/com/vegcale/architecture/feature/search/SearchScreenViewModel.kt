package com.vegcale.architecture.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vegcale.architecture.data.model.EarthquakeInfo
import com.vegcale.architecture.usecase.GetLatestEarthquakeInfoUseCase2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getLatestEarthquakeInfoUseCase: GetLatestEarthquakeInfoUseCase2
): ViewModel() {
    private var shouldRefresh = MutableStateFlow(true)

    val uiState: StateFlow<SearchScreenUiState> = shouldRefresh.transform {
        println("test nakao shouldRefresh: $it")
        if (!shouldRefresh.value) return@transform

        emit(
            withContext(context = Dispatchers.IO) {
                try {
                    SearchScreenUiState.Success(data = getLatestEarthquakeInfoUseCase())
                } catch (_: Exception) {
                    SearchScreenUiState.Failed
                } finally {
                    shouldRefresh.update { false }
                }
            }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchScreenUiState.Loading
        )

    fun refresh() {
        shouldRefresh.update { true }
    }
}

sealed interface SearchScreenUiState {
    object Loading: SearchScreenUiState
    data class Success(val data: List<EarthquakeInfo>): SearchScreenUiState
    object Failed: SearchScreenUiState
}
