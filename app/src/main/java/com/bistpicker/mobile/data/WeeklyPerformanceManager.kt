package com.bistpicker.mobile.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Immutable
@Serializable
data class WeeklyStockRecord(
    val ticker: String,
    val entryPrice: Double,
    val exitPrice: Double,
    val returnPct: Double,
)

@Immutable
@Serializable
data class WeeklyPerformanceRecord(
    val weekStartDate: String,
    val weekEndDate: String,
    val positions: List<WeeklyStockRecord>,
    val portfolioReturn: Double,
    val bist100StartPrice: Double,
    val bist100EndPrice: Double,
    val bist100Return: Double,
    val isCompleted: Boolean,
)

private const val LIVE_TRACKING_START_DATE = "2026-05-21"

/**
 * Build live portfolio history from the snapshot's central portfolio tables.
 *
 * Completed periods come from `portfolio_history`; the active period comes
 * from `open_positions` plus the in-memory Yahoo quote cache. No device-local
 * seed file is used, so Android, web, and fresh installs all see the same
 * completed history.
 */
suspend fun buildWeeklyPerformanceRecords(
    closedPositions: List<ClosedPosition>,
    openPositions: List<OpenPosition>,
    livePrices: Map<String, Double>,
    priceOnOrBefore: suspend (ticker: String, date: String) -> Double?,
    today: LocalDate = LocalDate.now(),
): List<WeeklyPerformanceRecord> {
    val completed = closedPositions
        .asSequence()
        .filter {
            !it.selectionDate.isNullOrBlank()
                && !it.exitDate.isNullOrBlank()
                && it.selectionDate >= LIVE_TRACKING_START_DATE
        }
        .groupBy { requireNotNull(it.selectionDate) to requireNotNull(it.exitDate) }
        .toSortedMap(compareBy<Pair<String, String>> { it.first }.thenBy { it.second })
        .mapNotNull { (period, positions) ->
            val stockRecords = positions.mapNotNull { position ->
                val entry = position.entryPrice
                val exit = position.exitPrice
                if (entry == null || entry <= 0.0 || exit == null) {
                    null
                } else {
                    WeeklyStockRecord(
                        ticker = position.ticker,
                        entryPrice = entry,
                        exitPrice = exit,
                        returnPct = exit / entry - 1.0,
                    )
                }
            }
            if (stockRecords.isEmpty()) return@mapNotNull null

            val bistStart = priceOnOrBefore("XU100", period.first) ?: return@mapNotNull null
            val bistEnd = priceOnOrBefore("XU100", period.second) ?: return@mapNotNull null

            WeeklyPerformanceRecord(
                weekStartDate = period.first,
                weekEndDate = period.second,
                positions = stockRecords,
                portfolioReturn = stockRecords.map { it.returnPct }.average(),
                bist100StartPrice = bistStart,
                bist100EndPrice = bistEnd,
                bist100Return = if (bistStart > 0.0) bistEnd / bistStart - 1.0 else 0.0,
                isCompleted = true,
            )
        }
        .toMutableList()

    val activeStart = openPositions
        .mapNotNull { it.selectionDate }
        .maxOrNull()

    if (activeStart != null && activeStart >= LIVE_TRACKING_START_DATE) {
        val activeStocks = openPositions
            .filter { it.selectionDate == activeStart }
            .mapNotNull { position ->
                val entry = position.entryPrice ?: position.snapshotPrice
                val latest = livePrices[position.ticker]
                    ?: position.currentPrice
                    ?: position.snapshotPrice
                if (entry == null || entry <= 0.0 || latest == null) {
                    null
                } else {
                    WeeklyStockRecord(
                        ticker = position.ticker,
                        entryPrice = entry,
                        exitPrice = latest,
                        returnPct = latest / entry - 1.0,
                    )
                }
            }

        if (activeStocks.isNotEmpty()) {
            val endDate = today.toString()
            val bistStart = priceOnOrBefore("XU100", activeStart)
            val bistEnd = livePrices["XU100"] ?: priceOnOrBefore("XU100", endDate)
            if (bistStart != null && bistStart > 0.0 && bistEnd != null) {
                completed.removeAll { it.weekStartDate == activeStart }
                completed += WeeklyPerformanceRecord(
                    weekStartDate = activeStart,
                    weekEndDate = endDate,
                    positions = activeStocks,
                    portfolioReturn = activeStocks.map { it.returnPct }.average(),
                    bist100StartPrice = bistStart,
                    bist100EndPrice = bistEnd,
                    bist100Return = bistEnd / bistStart - 1.0,
                    isCompleted = false,
                )
            }
        }
    }

    return completed.sortedBy { it.weekStartDate }
}
