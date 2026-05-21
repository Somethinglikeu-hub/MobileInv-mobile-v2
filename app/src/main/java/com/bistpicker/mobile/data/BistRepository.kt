package com.bistpicker.mobile.data

import kotlinx.coroutines.flow.Flow

/**
 * UI-facing read API. The implementation maps Room entities to the view
 * models in [Models.kt]. We keep the interface deliberately narrow so a
 * future remote/online variant (or an in-memory fake for tests) can drop
 * in without touching ViewModels.
 */
interface BistRepository {

    fun observeHome(): Flow<HomeData>

    fun observeSnapshotInfo(): Flow<SnapshotInfo?>

    suspend fun queryScoring(filters: ScoringFilters, page: Int, pageSize: Int): ScoringPage

    suspend fun loadFilterOptions(): FilterOptions

    suspend fun loadDetail(ticker: String): StockDetail?

    /** Refresh in-memory live prices for the given tickers. */
    suspend fun refreshLivePrices(tickers: List<String>)
}
