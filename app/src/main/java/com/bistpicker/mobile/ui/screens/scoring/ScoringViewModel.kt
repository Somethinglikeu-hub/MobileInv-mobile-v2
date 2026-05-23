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

import com.bistpicker.mobile.data.RiskTier
import com.bistpicker.mobile.data.ScoringSortOrder

class ScoringViewModel(private val repository: BistRepository) : ViewModel() {

    private val _filters = MutableStateFlow(ScoringFilters())
    val filters: StateFlow<ScoringFilters> = _filters.asStateFlow()

    private val _sectors = MutableStateFlow<List<String>>(emptyList())
    val sectors: StateFlow<List<String>> = _sectors.asStateFlow()

    private val _uiState = MutableStateFlow<ScoringUiState>(ScoringUiState.Loading)
    val uiState: StateFlow<ScoringUiState> = _uiState.asStateFlow()

    init {
        loadScoring()
        loadSectors()
    }

    private fun loadSectors() {
        viewModelScope.launch {
            try {
                val opts = repository.loadFilterOptions()
                _sectors.value = opts.sectors
            } catch (e: Exception) {
                // Keep empty list if error
            }
        }
    }

    fun setViewMode(mode: ScoringViewMode) {
        _filters.value = _filters.value.copy(mode = mode)
        loadScoring()
    }

    fun setSearch(query: String) {
        _filters.value = _filters.value.copy(search = query.takeIf { it.isNotBlank() })
        loadScoring()
    }

    fun setSector(sector: String?) {
        _filters.value = _filters.value.copy(sector = sector)
        loadScoring()
    }

    fun setRisk(risk: RiskTier?) {
        _filters.value = _filters.value.copy(risk = risk)
        loadScoring()
    }

    fun setOnlyBist100(only: Boolean) {
        _filters.value = _filters.value.copy(onlyBist100 = only)
        loadScoring()
    }

    fun setSortBy(sortBy: ScoringSortOrder) {
        _filters.value = _filters.value.copy(sortBy = sortBy)
        loadScoring()
    }

    fun clearFilters() {
        _filters.value = ScoringFilters(search = _filters.value.search, mode = _filters.value.mode)
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
