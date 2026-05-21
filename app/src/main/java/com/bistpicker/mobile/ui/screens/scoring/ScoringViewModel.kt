package com.bistpicker.mobile.ui.screens.scoring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bistpicker.mobile.data.BistRepository
import com.bistpicker.mobile.data.ScoringFilters
import com.bistpicker.mobile.data.ScoringPage
import com.bistpicker.mobile.data.ScoringViewMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScoringViewModel(private val repository: BistRepository) : ViewModel() {

    private val _filters = MutableStateFlow(ScoringFilters())
    val filters: StateFlow<ScoringFilters> = _filters.asStateFlow()

    private val _uiState = MutableStateFlow<ScoringUiState>(ScoringUiState.Loading)
    val uiState: StateFlow<ScoringUiState> = _uiState.asStateFlow()

    init {
        loadScoring()
    }

    fun setViewMode(mode: ScoringViewMode) {
        _filters.value = _filters.value.copy(mode = mode)
        loadScoring()
    }

    fun setSearch(query: String) {
        _filters.value = _filters.value.copy(search = query)
        loadScoring()
    }

    fun loadScoring() {
        viewModelScope.launch {
            _uiState.value = ScoringUiState.Loading
            val page = repository.queryScoring(_filters.value, page = 0, pageSize = 100)
            _uiState.value = ScoringUiState.Success(page)
        }
    }

    class Factory(private val repository: BistRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ScoringViewModel(repository) as T
        }
    }
}

sealed interface ScoringUiState {
    object Loading : ScoringUiState
    data class Success(val page: ScoringPage) : ScoringUiState
}
