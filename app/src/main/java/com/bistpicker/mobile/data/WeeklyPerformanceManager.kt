package com.bistpicker.mobile.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Immutable
@Serializable
data class WeeklyStockRecord(
    val ticker: String,
    val entryPrice: Double,
    val exitPrice: Double,
    val returnPct: Double
)

@Immutable
@Serializable
data class WeeklyPerformanceRecord(
    val weekStartDate: String, // "yyyy-MM-dd"
    val positions: List<WeeklyStockRecord>,
    val portfolioReturn: Double,
    val bist100StartPrice: Double,
    val bist100EndPrice: Double,
    val bist100Return: Double,
    val isCompleted: Boolean
)


class WeeklyPerformanceManager(
    private val context: Context,
    private val json: Json
) {
    private val file = File(context.filesDir, "weekly_performance_history.json")
    private var cachedRecords: List<WeeklyPerformanceRecord>? = null

    fun loadRecords(): List<WeeklyPerformanceRecord> {
        val cached = cachedRecords
        if (cached != null) return cached

        if (!file.exists()) {
            val initial = seedInitialRecords()
            saveRecords(initial)
            return initial
        }
        val records = try {
            val content = file.readText()
            json.decodeFromString<List<WeeklyPerformanceRecord>>(content)
        } catch (e: Exception) {
            Log.e("WeeklyPerformance", "Error reading records", e)
            val initial = seedInitialRecords()
            saveRecords(initial)
            initial
        }
        cachedRecords = records
        return records
    }

    fun saveRecords(records: List<WeeklyPerformanceRecord>) {
        cachedRecords = records
        try {
            val content = json.encodeToString(records)
            file.writeText(content)
        } catch (e: Exception) {
            Log.e("WeeklyPerformance", "Error writing records", e)
        }
    }

    private fun seedInitialRecords(): List<WeeklyPerformanceRecord> {
        // Seed the current week starting 2026-05-18 as the active week.
        val mondayDate = "2026-05-18"
        val positions = listOf(
            WeeklyStockRecord("PCILT", 32.48, 32.48, 0.0),
            WeeklyStockRecord("KIMMR", 17.29, 17.29, 0.0),
            WeeklyStockRecord("ASELS", 428.00, 428.00, 0.0),
            WeeklyStockRecord("LILAK", 36.08, 36.08, 0.0),
            WeeklyStockRecord("TCKRC", 111.80, 111.80, 0.0)
        )
        return listOf(
            WeeklyPerformanceRecord(
                weekStartDate = mondayDate,
                positions = positions,
                portfolioReturn = 0.0,
                bist100StartPrice = 14029.54,
                bist100EndPrice = 14029.54,
                bist100Return = 0.0,
                isCompleted = false
            )
        )
    }

    fun updateActiveWeek(
        currentMondayDate: String,
        dbPositions: List<OpenPosition>,
        livePrices: Map<String, Double>,
        bist100MondayPrice: Double
    ): List<WeeklyPerformanceRecord> {
        val currentRecords = loadRecords().toMutableList()
        var updated = false

        // Determine if we are past BIST Friday closing time (18:15 TR Time - GMT+3)
        val trCal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"))
        val trDay = trCal.get(Calendar.DAY_OF_WEEK)
        val trHour = trCal.get(Calendar.HOUR_OF_DAY)
        val trMin = trCal.get(Calendar.MINUTE)
        val isPastFridayClose = when (trDay) {
            Calendar.FRIDAY -> (trHour > 18) || (trHour == 18 && trMin >= 15)
            Calendar.SATURDAY, Calendar.SUNDAY -> true
            else -> false
        }

        // 1. Mark older uncompleted weeks as completed/frozen
        for (i in currentRecords.indices) {
            val rec = currentRecords[i]
            if (rec.weekStartDate != currentMondayDate && !rec.isCompleted) {
                currentRecords[i] = rec.copy(isCompleted = true)
                updated = true
            }
        }

        // 2. Find or create the current active week
        val index = currentRecords.indexOfFirst { it.weekStartDate == currentMondayDate }
        if (index == -1) {
            val stockRecords = dbPositions.map { pos ->
                val entry = pos.entryPrice ?: pos.snapshotPrice ?: 1.0
                val live = livePrices[pos.ticker] ?: entry
                WeeklyStockRecord(
                    ticker = pos.ticker,
                    entryPrice = entry,
                    exitPrice = live,
                    returnPct = if (entry > 0) (live / entry - 1.0) else 0.0
                )
            }
            val liveBist100 = livePrices["XU100"] ?: bist100MondayPrice
            val newRecord = WeeklyPerformanceRecord(
                weekStartDate = currentMondayDate,
                positions = stockRecords,
                portfolioReturn = if (stockRecords.isNotEmpty()) stockRecords.map { it.returnPct }.average() else 0.0,
                bist100StartPrice = bist100MondayPrice,
                bist100EndPrice = liveBist100,
                bist100Return = if (bist100MondayPrice > 0) (liveBist100 / bist100MondayPrice - 1.0) else 0.0,
                isCompleted = isPastFridayClose
            )
            currentRecords.add(newRecord)
            updated = true
        } else {
            val rec = currentRecords[index]
            if (!rec.isCompleted) {
                // If the records positions list is empty (e.g. initial empty seed), populate it
                val shouldPopulate = rec.positions.isEmpty() || rec.positions.all { it.entryPrice == 1.0 && it.exitPrice == 1.0 }
                val basePositions = if (shouldPopulate) {
                    dbPositions.map { pos ->
                        val entry = pos.entryPrice ?: pos.snapshotPrice ?: 1.0
                        WeeklyStockRecord(pos.ticker, entry, entry, 0.0)
                    }
                } else {
                    rec.positions
                }

                val updatedStocks = basePositions.map { stock ->
                    val live = livePrices[stock.ticker]
                    if (live != null) {
                        stock.copy(
                            exitPrice = live,
                            returnPct = if (stock.entryPrice > 0) (live / stock.entryPrice - 1.0) else 0.0
                        )
                    } else {
                        stock
                    }
                }
                val liveBist100 = livePrices["XU100"] ?: rec.bist100EndPrice
                val updatedRec = rec.copy(
                    positions = updatedStocks,
                    portfolioReturn = if (updatedStocks.isNotEmpty()) updatedStocks.map { it.returnPct }.average() else 0.0,
                    bist100EndPrice = liveBist100,
                    bist100Return = if (rec.bist100StartPrice > 0) (liveBist100 / rec.bist100StartPrice - 1.0) else 0.0,
                    isCompleted = isPastFridayClose
                )
                currentRecords[index] = updatedRec
                
                // Only save to disk if we initialized/populated positions or if the week completed
                if (shouldPopulate || isPastFridayClose) {
                    updated = true
                } else {
                    // Update cache only (avoid disk write)
                    cachedRecords = currentRecords
                }
            }
        }

        if (updated) {
            saveRecords(currentRecords)
        }
        return currentRecords
    }
}
