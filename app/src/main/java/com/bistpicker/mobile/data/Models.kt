package com.bistpicker.mobile.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * View-layer types. Decoupled from Room entities so the Compose layer
 * doesn't need to import Room and so we can evolve the snapshot schema
 * without rewriting screens.
 */

enum class AppearanceMode { SYSTEM, LIGHT, DARK }

enum class CashState { NORMAL, CAUTION, DEFENSIVE, RISK_OFF, UNKNOWN;
    companion object {
        fun fromString(raw: String?): CashState = when (raw?.uppercase()) {
            "NORMAL" -> NORMAL
            "CAUTION" -> CAUTION
            "DEFENSIVE" -> DEFENSIVE
            "RISK_OFF" -> RISK_OFF
            else -> UNKNOWN
        }
    }
}

enum class RiskTier { LOW, MEDIUM, HIGH, UNKNOWN;
    companion object {
        fun fromString(raw: String?): RiskTier = when (raw?.uppercase()) {
            "LOW" -> LOW
            "MEDIUM" -> MEDIUM
            "HIGH" -> HIGH
            else -> UNKNOWN
        }
    }
}

/** Buckets the picker assigns to companies that miss strict ALPHA Core. */
enum class ResearchBucket(val displayKey: String) {
    AlphaCore("ALPHA Core"),
    QualityShadow("Quality Shadow"),
    FreeFloatShadow("Free-Float Shadow"),
    NonCoreResearch("Non-Core Research"),
    DataUnscorable("Data-Unscorable"),
    Excluded("Excluded"),
    Unknown("");

    companion object {
        fun fromString(raw: String?): ResearchBucket = when (raw) {
            "ALPHA Core" -> AlphaCore
            "Quality Shadow" -> QualityShadow
            "Free-Float Shadow" -> FreeFloatShadow
            "Non-Core Research" -> NonCoreResearch
            "Data-Unscorable" -> DataUnscorable
            "Excluded" -> Excluded
            else -> Unknown
        }
    }
}

/** Toolbar / segmented filter for the Liste screen. */
enum class ScoringViewMode { ALPHA_CORE, ALPHA_X, RESEARCH, MODEL, ALL }

data class ScoringFilters(
    val mode: ScoringViewMode = ScoringViewMode.ALPHA_CORE,
    val sector: String? = null,
    val risk: RiskTier? = null,
    val onlyBist100: Boolean = false,
    val minScore: Double? = null,
    val search: String? = null,
)

@Serializable
data class ReasonFactor(
    val factor: String,
    val label: String,
    val value: Double,
)

data class HomeMacro(
    val date: String?,
    val policyRatePct: Double?,
    val cpiYoyPct: Double?,
    val usdTryRate: Double?,
    val regime: String?,
)

data class HomeCash(
    val state: CashState,
    val cashPct: Double?,
    val daysInState: Int?,
    val targetState: CashState,
    val notes: String?,
    val rawSignal: Int?,
)

data class HomePerformance(
    val totalReturnAvg: Double?,
    val activeReturnAvg: Double?,
    val winRate: Double?,
    val benchmarkYtd: Double?,
)

data class OpenPosition(
    val ticker: String,
    val name: String?,
    val portfolio: String?,
    val entryPrice: Double?,
    val currentPrice: Double?,
    val pnlPct: Double?,
    val targetPrice: Double?,
    val stopLossPrice: Double?,
    val compositeScore: Double?,
    val daysHeld: Int?,
    val selectionDate: String?,
    val reasons: List<ReasonFactor>,
    val qualityFlags: List<String>,
    val dcfMosPct: Double?,
)

data class ClosedPosition(
    val ticker: String,
    val name: String?,
    val selectionDate: String?,
    val exitDate: String?,
    val entryPrice: Double?,
    val exitPrice: Double?,
    val pnlPct: Double?,
    val exitReason: String?,
    val holdingDays: Int?,
)

enum class TradeAction { BUY, SELL, HOLD }

data class SuggestedAction(
    val ticker: String,
    val action: TradeAction,
    val reason: String?,
)

data class HomeData(
    val macro: HomeMacro?,
    val cash: HomeCash?,
    val performance: HomePerformance?,
    val openPositions: List<OpenPosition>,
    val history: List<ClosedPosition>,
    val suggestions: List<SuggestedAction>,
    val weekStart: String?,
    val weekEnd: String?,
)

data class ScoringRow(
    val ticker: String,
    val name: String?,
    val sector: String?,
    val type: String?,
    val isBist100: Boolean,
    val rankingScore: Double?,
    val rankingSource: String?,
    val alpha: Double?,
    val alphaXScore: Double?,
    val alphaXRank: Int?,
    val alphaCoreEligible: Boolean,
    val alphaXEligible: Boolean,
    val researchBucket: ResearchBucket,
    val primaryBlocker: String?,
    val streak: Int?,
    val risk: RiskTier,
    val dataCompleteness: Double?,
    val above200ma: Boolean?,
    val technical: Double?,
    val momentum: Double?,
    val buffett: Double?,
    val dcfMos: Double?,
)

data class ScoringPage(
    val rows: List<ScoringRow>,
    val totalCount: Int,
    val hasMore: Boolean,
)

data class FactorBreakdown(
    val buffett: Double?,
    val graham: Double?,
    val piotroski: Double?,
    val piotroskiRaw: Int?,
    val magicFormula: Double?,
    val lynchPeg: Double?,
    val dcfMos: Double?,
    val momentum: Double?,
    val technical: Double?,
    val dividend: Double?,
)

data class DcfDetail(
    val intrinsicValue: Double?,
    val growthRatePct: Double?,
    val discountRatePct: Double?,
    val terminalGrowthPct: Double?,
    val mosPct: Double?,
)

data class FactorHistoryPoint(
    val quarterEnd: String,
    val buffett: Double?,
    val graham: Double?,
    val piotroski: Double?,
    val momentum: Double?,
    val technical: Double?,
    val dcfMos: Double?,
    val compositeAlpha: Double?,
)

data class PricePoint(
    val date: String,
    val open: Double?,
    val high: Double?,
    val low: Double?,
    val close: Double?,
    val volume: Long?,
    val adjustedClose: Double?,
)

data class FinancialsSnapshot(
    val periodEnd: String?,
    val reportedNetIncome: Double?,
    val adjustedNetIncome: Double?,
    val ownerEarnings: Double?,
    val freeCashFlow: Double?,
    val roeAdjusted: Double?,
    val roaAdjusted: Double?,
    val epsAdjusted: Double?,
    val realEpsGrowthPct: Double?,
    val relatedPartyRevenuePct: Double?,
)

data class SectorBenchmark(
    val sector: String,
    val roeMedian: Double?,
    val roaMedian: Double?,
    val netMarginMedian: Double?,
    val companyCount: Int?,
)

data class StockDetail(
    val ticker: String,
    val name: String?,
    val sector: String?,
    val type: String?,
    val isBist100: Boolean,
    val freeFloatPct: Double?,
    val rankingScore: Double?,
    val rankingSource: String?,
    val alpha: Double?,
    val alphaXScore: Double?,
    val alphaXRank: Int?,
    val researchBucket: ResearchBucket,
    val aiInsight: String?,
    val alphaReason: String?,
    val primaryBlocker: String?,
    val streak: Int?,
    val risk: RiskTier,
    val dataCompleteness: Double?,
    val factors: FactorBreakdown,
    val dcf: DcfDetail,
    val financials: FinancialsSnapshot?,
    val factorHistory: List<FactorHistoryPoint>,
    val priceHistory: List<PricePoint>,
    val openPosition: OpenPosition?,
    val qualityFlags: List<String>,
    val sectorBenchmark: SectorBenchmark?,
)

data class SnapshotInfo(
    val schemaVersion: Int,
    val snapshotDate: String?,
    val exportedAt: String,
    val companyCount: Int,
    val priceHistoryDays: Int,
)

data class FilterOptions(
    val sectors: List<String>,
)
