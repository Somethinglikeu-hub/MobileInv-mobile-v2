package com.bistpicker.mobile.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class WeeklyPerformanceCalculatorTest {

    @Test
    fun buildsCompletedAndActivePeriodsFromCentralPortfolioData() = runBlocking {
        val closed = listOf(
            ClosedPosition(
                ticker = "AAA",
                name = null,
                selectionDate = "2026-06-01",
                exitDate = "2026-06-08",
                entryPrice = 10.0,
                exitPrice = 11.0,
                pnlPct = 10.0,
                exitReason = "REBALANCE",
                holdingDays = 7,
            ),
            ClosedPosition(
                ticker = "BBB",
                name = null,
                selectionDate = "2026-06-01",
                exitDate = "2026-06-08",
                entryPrice = 20.0,
                exitPrice = 18.0,
                pnlPct = -10.0,
                exitReason = "REBALANCE",
                holdingDays = 7,
            ),
        )
        val open = listOf(
            OpenPosition(
                ticker = "CCC",
                name = null,
                portfolio = "ALPHA",
                entryPrice = 25.0,
                currentPrice = 25.0,
                snapshotPrice = 25.0,
                pnlPct = 0.0,
                targetPrice = null,
                stopLossPrice = null,
                compositeScore = null,
                daysHeld = 0,
                selectionDate = "2026-06-08",
                reasons = emptyList(),
                qualityFlags = emptyList(),
                dcfMosPct = null,
            ),
        )
        val benchmarkPrices = mapOf(
            "2026-06-01" to 100.0,
            "2026-06-08" to 102.0,
            "2026-06-10" to 103.0,
        )

        val records = buildWeeklyPerformanceRecords(
            closedPositions = closed,
            openPositions = open,
            livePrices = mapOf("CCC" to 27.5, "XU100" to 103.0),
            priceOnOrBefore = { ticker, date ->
                if (ticker == "XU100") benchmarkPrices[date] else null
            },
            today = LocalDate.of(2026, 6, 10),
        )

        assertEquals(2, records.size)
        assertTrue(records[0].isCompleted)
        assertEquals(0.0, records[0].portfolioReturn, 1e-9)
        assertEquals(0.02, records[0].bist100Return, 1e-9)
        assertFalse(records[1].isCompleted)
        assertEquals(0.10, records[1].portfolioReturn, 1e-9)
        assertEquals(103.0 / 102.0 - 1.0, records[1].bist100Return, 1e-9)
    }
}
