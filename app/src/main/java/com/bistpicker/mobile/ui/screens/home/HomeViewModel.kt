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

class HomeViewModel(
    private val repository: BistRepository,
    private val syncStore: SnapshotSyncStateStore
) : ViewModel() {

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
