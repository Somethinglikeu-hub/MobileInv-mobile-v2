package com.bistpicker.mobile.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// All entities mirror the SQLite tables produced by Python's
// `bist_picker.mobile_snapshot` script.

@Entity(tableName = "snapshot_metadata")
data class SnapshotMetadataEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("schema_version") val schemaVersion: Int,
    @ColumnInfo("exported_at") val exportedAt: String,
    @ColumnInfo("snapshot_date") val snapshotDate: String?,
    @ColumnInfo("latest_price_date") val latestPriceDate: String?,
    @ColumnInfo("source_db_path") val sourceDbPath: String?,
    @ColumnInfo("company_count") val companyCount: Int,
    @ColumnInfo("scoring_row_count") val scoringRowCount: Int,
    @ColumnInfo("price_history_days") val priceHistoryDays: Int,
)

@Entity(tableName = "home_summary")
data class HomeSummaryEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("total_return_avg") val totalReturnAvg: Double?,
    @ColumnInfo("active_return_avg") val activeReturnAvg: Double?,
    @ColumnInfo("win_rate") val winRate: Double?,
    @ColumnInfo("benchmark_ytd") val benchmarkYtd: Double?,
    @ColumnInfo("macro_date") val macroDate: String?,
    @ColumnInfo("policy_rate_pct") val policyRatePct: Double?,
    @ColumnInfo("cpi_yoy_pct") val cpiYoyPct: Double?,
    @ColumnInfo("usdtry_rate") val usdTryRate: Double?,
    val regime: String?,
    @ColumnInfo("cash_state") val cashState: String?,
    @ColumnInfo("cash_pct") val cashPct: Double?,
    @ColumnInfo("cash_days_in_state") val cashDaysInState: Int?,
    @ColumnInfo("cash_last_transition_date") val cashLastTransitionDate: String?,
    @ColumnInfo("cash_target_state") val cashTargetState: String?,
    @ColumnInfo("cash_notes") val cashNotes: String?,
    @ColumnInfo("cash_raw_signal") val cashRawSignal: Int?,
)

@Entity(tableName = "open_positions")
data class OpenPositionEntity(
    @PrimaryKey @ColumnInfo("sort_order") val sortOrder: Int,
    val portfolio: String?,
    val ticker: String,
    val name: String?,
    @ColumnInfo("company_id") val companyId: Int?,
    @ColumnInfo("entry_price") val entryPrice: Double?,
    @ColumnInfo("current_price") val currentPrice: Double?,
    @ColumnInfo("pnl_pct") val pnlPct: Double?,
    @ColumnInfo("target_price") val targetPrice: Double?,
    @ColumnInfo("stop_loss_price") val stopLossPrice: Double?,
    @ColumnInfo("stop_pct_from_entry") val stopPctFromEntry: Double?,
    @ColumnInfo("composite_score") val compositeScore: Double?,
    @ColumnInfo("selection_date") val selectionDate: String?,
    @ColumnInfo("days_held") val daysHeld: Int?,
    @ColumnInfo("reason_top_factors_json") val reasonTopFactorsJson: String?,
    @ColumnInfo("quality_flags_json") val qualityFlagsJson: String?,
    @ColumnInfo("dcf_margin_of_safety_pct") val dcfMosPct: Double?,
    @ColumnInfo("dcf_intrinsic_value") val dcfIntrinsicValue: Double?,
    @ColumnInfo("dcf_growth_rate_pct") val dcfGrowthRatePct: Double?,
    @ColumnInfo("dcf_discount_rate_pct") val dcfDiscountRatePct: Double?,
    @ColumnInfo("dcf_terminal_growth_pct") val dcfTerminalGrowthPct: Double?,
)

@Entity(tableName = "portfolio_history")
data class PortfolioHistoryEntity(
    @PrimaryKey @ColumnInfo("sort_order") val sortOrder: Int,
    val portfolio: String?,
    val ticker: String,
    val name: String?,
    @ColumnInfo("selection_date") val selectionDate: String?,
    @ColumnInfo("exit_date") val exitDate: String?,
    @ColumnInfo("entry_price") val entryPrice: Double?,
    @ColumnInfo("exit_price") val exitPrice: Double?,
    @ColumnInfo("pnl_pct") val pnlPct: Double?,
    @ColumnInfo("exit_reason") val exitReason: String?,
    @ColumnInfo("holding_days") val holdingDays: Int?,
)

@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey val id: Int,
    val ticker: String,
    val name: String?,
    @ColumnInfo("company_type") val companyType: String?,
    @ColumnInfo("sector_bist") val sectorBist: String?,
    @ColumnInfo("sector_custom") val sectorCustom: String?,
    @ColumnInfo("is_bist100") val isBist100: Int,
    @ColumnInfo("is_ipo") val isIpo: Int,
    @ColumnInfo("free_float_pct") val freeFloatPct: Double?,
    @ColumnInfo("listing_date") val listingDate: String?,
    @ColumnInfo("is_active") val isActive: Int,
)

@Entity(tableName = "scoring_latest")
data class ScoringLatestEntity(
    @PrimaryKey @ColumnInfo("company_id") val companyId: Int,
    val ticker: String,
    val name: String?,
    val sector: String?,
    val type: String?,
    @ColumnInfo("is_bist100") val isBist100: Int,
    @ColumnInfo("is_active") val isActive: Int,
    @ColumnInfo("free_float_pct") val freeFloatPct: Double?,
    @ColumnInfo("avg_volume_try") val avgVolumeTry: Double?,
    @ColumnInfo("ranking_score") val rankingScore: Double?,
    @ColumnInfo("ranking_source") val rankingSource: String?,
    @ColumnInfo("model_score") val modelScore: Double?,
    val alpha: Double?,
    @ColumnInfo("alpha_x_score") val alphaXScore: Double?,
    @ColumnInfo("alpha_x_rank") val alphaXRank: Double?,
    @ColumnInfo("alpha_x_confidence") val alphaXConfidence: Double?,
    @ColumnInfo("alpha_core_eligible") val alphaCoreEligible: Int,
    @ColumnInfo("alpha_x_eligible") val alphaXEligible: Int,
    @ColumnInfo("alpha_reason") val alphaReason: String?,
    @ColumnInfo("alpha_primary_blocker") val alphaPrimaryBlocker: String?,
    @ColumnInfo("alpha_research_bucket") val alphaResearchBucket: String?,
    @ColumnInfo("alpha_snapshot_streak") val alphaSnapshotStreak: Int?,
    @ColumnInfo("ai_insight") val aiInsight: String?,
    val risk: String?,
    @ColumnInfo("data_completeness") val dataCompleteness: Double?,
    @ColumnInfo("scoring_date") val scoringDate: String?,
    @ColumnInfo("model_used") val modelUsed: String?,
    val buffett: Double?,
    val graham: Double?,
    val piotroski: Double?,
    @ColumnInfo("piotroski_raw") val piotroskiRaw: Int?,
    @ColumnInfo("magic_formula") val magicFormula: Double?,
    @ColumnInfo("lynch_peg") val lynchPeg: Double?,
    @ColumnInfo("dcf_mos") val dcfMos: Double?,
    val momentum: Double?,
    val insider: Double?,
    val technical: Double?,
    val dividend: Double?,
    val beta: Double?,
    val delta: Double?,
    @ColumnInfo("quality_flags_json") val qualityFlagsJson: String?,
    @ColumnInfo("dcf_intrinsic_value") val dcfIntrinsicValue: Double?,
    @ColumnInfo("dcf_growth_rate_pct") val dcfGrowthRatePct: Double?,
    @ColumnInfo("dcf_discount_rate_pct") val dcfDiscountRatePct: Double?,
    @ColumnInfo("dcf_terminal_growth_pct") val dcfTerminalGrowthPct: Double?,
)

@Entity(tableName = "sector_benchmarks")
data class SectorBenchmarkEntity(
    @PrimaryKey val sector: String,
    @ColumnInfo("roe_median") val roeMedian: Double?,
    @ColumnInfo("roa_median") val roaMedian: Double?,
    @ColumnInfo("net_margin_median") val netMarginMedian: Double?,
    @ColumnInfo("company_count") val companyCount: Int?,
)

@Entity(tableName = "adjusted_metrics_latest")
data class AdjustedMetricsEntity(
    @PrimaryKey @ColumnInfo("company_id") val companyId: Int,
    @ColumnInfo("period_end") val periodEnd: String?,
    @ColumnInfo("reported_net_income") val reportedNetIncome: Double?,
    @ColumnInfo("monetary_gain_loss") val monetaryGainLoss: Double?,
    @ColumnInfo("adjusted_net_income") val adjustedNetIncome: Double?,
    @ColumnInfo("owner_earnings") val ownerEarnings: Double?,
    @ColumnInfo("free_cash_flow") val freeCashFlow: Double?,
    @ColumnInfo("roe_adjusted") val roeAdjusted: Double?,
    @ColumnInfo("roa_adjusted") val roaAdjusted: Double?,
    @ColumnInfo("eps_adjusted") val epsAdjusted: Double?,
    @ColumnInfo("real_eps_growth_pct") val realEpsGrowthPct: Double?,
    @ColumnInfo("related_party_revenue_pct") val relatedPartyRevenuePct: Double?,
    @ColumnInfo("maintenance_capex") val maintenanceCapex: Double?,
    @ColumnInfo("growth_capex") val growthCapex: Double?,
)

@Entity(tableName = "price_history_730d", primaryKeys = ["company_id", "date"])
data class PriceHistoryEntity(
    @ColumnInfo("company_id") val companyId: Int,
    val date: String,
    val open: Double?,
    val high: Double?,
    val low: Double?,
    val close: Double?,
    val volume: Long?,
    @ColumnInfo("adjusted_close") val adjustedClose: Double?,
)

@Entity(tableName = "factor_history_quarterly", primaryKeys = ["company_id", "quarter_end"])
data class FactorHistoryEntity(
    @ColumnInfo("company_id") val companyId: Int,
    @ColumnInfo("quarter_end") val quarterEnd: String,
    @ColumnInfo("scoring_date") val scoringDate: String,
    val buffett: Double?,
    val graham: Double?,
    val piotroski: Double?,
    @ColumnInfo("magic_formula") val magicFormula: Double?,
    @ColumnInfo("lynch_peg") val lynchPeg: Double?,
    @ColumnInfo("dcf_mos") val dcfMos: Double?,
    val momentum: Double?,
    val technical: Double?,
    val dividend: Double?,
    @ColumnInfo("composite_alpha") val compositeAlpha: Double?,
    @ColumnInfo("data_completeness") val dataCompleteness: Double?,
)
