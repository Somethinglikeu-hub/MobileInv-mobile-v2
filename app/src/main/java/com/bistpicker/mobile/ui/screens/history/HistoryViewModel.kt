package com.bistpicker.mobile.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bistpicker.mobile.data.BistRepository
import com.bistpicker.mobile.data.ClosedPosition
import com.bistpicker.mobile.data.ModelPerformancePoint
import com.bistpicker.mobile.data.OpenPosition
import com.bistpicker.mobile.data.WeeklyPerformanceRecord
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: BistRepository
) : ViewModel() {

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val homeData = repository.observeHome().first()
                val tickers = homeData.openPositions.map { it.ticker }
                if (tickers.isNotEmpty()) {
                    repository.refreshLivePrices(tickers)
                }
            } catch (e: Exception) {
                // Ignore initial load exceptions
            }
        }
    }

    val uiState: StateFlow<HistoryUiState> = repository.observeHome()
        .map { data ->
            HistoryUiState.Success(
                performance = data.modelPerformance,
                closedPositions = data.history,
                openPositions = data.openPositions,
                weeklyPerformance = data.weeklyPerformance
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryUiState.Loading
        )

    class Factory(
        private val repository: BistRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HistoryViewModel(repository) as T
        }
    }
}

sealed interface HistoryUiState {
    object Loading : HistoryUiState
    data class Success(
        val performance: List<ModelPerformancePoint>,
        val closedPositions: List<ClosedPosition>,
        val openPositions: List<OpenPosition>,
        val weeklyPerformance: List<WeeklyPerformanceRecord>
    ) : HistoryUiState
}

