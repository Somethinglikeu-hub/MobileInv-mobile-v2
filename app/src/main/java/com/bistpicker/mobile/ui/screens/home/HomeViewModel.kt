package com.bistpicker.mobile.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bistpicker.mobile.data.BistRepository
import com.bistpicker.mobile.data.HomeData
import com.bistpicker.mobile.data.SnapshotInfo
import com.bistpicker.mobile.data.sync.SnapshotSyncStateStore
import com.bistpicker.mobile.data.sync.SyncState
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: BistRepository,
    private val syncStore: SnapshotSyncStateStore
) : ViewModel() {

    init {
        // Initial live price fetch
        refresh()
    }

    val uiState: StateFlow<HomeUiState> = combine(
        repository.observeHome(),
        repository.observeSnapshotInfo(),
        syncStore.state
    ) { home, info, sync ->
        Log.d("HomeViewModel", "Emitting Success: ${home.openPositions.size} positions, ${home.suggestions.size} suggestions")
        HomeUiState.Success(home, info, sync)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun refresh() {
        Log.d("HomeViewModel", "Manual refresh triggered")
        viewModelScope.launch {
            // Wait for initial data if needed
            val homeData = repository.observeHome().first()
            val tickers = (homeData.openPositions.map { it.ticker } + homeData.suggestions.map { it.ticker }).distinct()
            if (tickers.isNotEmpty()) {
                repository.refreshLivePrices(tickers)
            }
        }
    }

    class Factory(
        private val repository: BistRepository,
        private val syncStore: SnapshotSyncStateStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository, syncStore) as T
        }
    }
}

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(
        val data: HomeData,
        val info: SnapshotInfo?,
        val sync: SyncState?
    ) : HomeUiState
}
