package com.bistpicker.mobile.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bistpicker.mobile.data.BistRepository
import com.bistpicker.mobile.data.StockDetail
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(
    private val ticker: String,
    private val repository: BistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        observeDetail()
        refreshLivePrices()
    }

    private fun observeDetail() {
        viewModelScope.launch {
            repository.observeDetail(ticker)
                .collect { detail ->
                    if (detail != null) {
                        _uiState.value = DetailUiState.Success(detail)
                    } else {
                        _uiState.value = DetailUiState.Error("Hisse bulunamadi")
                    }
                }
        }
    }

    private fun refreshLivePrices() {
        viewModelScope.launch {
            repository.refreshLivePrices(listOf(ticker))
        }
    }

    class Factory(private val ticker: String, private val repository: BistRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(ticker, repository) as T
        }
    }
}

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val detail: StockDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
