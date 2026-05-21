package com.bistpicker.mobile.`data`.local

import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SnapshotDao_Impl(
  __db: RoomDatabase,
) : SnapshotDao {
  private val __db: RoomDatabase
  init {
    this.__db = __db
  }

  public override fun observeMetadata(): Flow<SnapshotMetadataEntity?> {
    val _sql: String = "SELECT * FROM snapshot_metadata WHERE id = 1"
    return createFlow(__db, false, arrayOf("snapshot_metadata")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfSchemaVersion: Int = getColumnIndexOrThrow(_stmt, "schema_version")
        val _cursorIndexOfExportedAt: Int = getColumnIndexOrThrow(_stmt, "exported_at")
        val _cursorIndexOfSnapshotDate: Int = getColumnIndexOrThrow(_stmt, "snapshot_date")
        val _cursorIndexOfLatestPriceDate: Int = getColumnIndexOrThrow(_stmt, "latest_price_date")
        val _cursorIndexOfSourceDbPath: Int = getColumnIndexOrThrow(_stmt, "source_db_path")
        val _cursorIndexOfCompanyCount: Int = getColumnIndexOrThrow(_stmt, "company_count")
        val _cursorIndexOfScoringRowCount: Int = getColumnIndexOrThrow(_stmt, "scoring_row_count")
        val _cursorIndexOfPriceHistoryDays: Int = getColumnIndexOrThrow(_stmt, "price_history_days")
        val _result: SnapshotMetadataEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpSchemaVersion: Int
          _tmpSchemaVersion = _stmt.getLong(_cursorIndexOfSchemaVersion).toInt()
          val _tmpExportedAt: String
          _tmpExportedAt = _stmt.getText(_cursorIndexOfExportedAt)
          val _tmpSnapshotDate: String?
          if (_stmt.isNull(_cursorIndexOfSnapshotDate)) {
            _tmpSnapshotDate = null
          } else {
            _tmpSnapshotDate = _stmt.getText(_cursorIndexOfSnapshotDate)
          }
          val _tmpLatestPriceDate: String?
          if (_stmt.isNull(_cursorIndexOfLatestPriceDate)) {
            _tmpLatestPriceDate = null
          } else {
            _tmpLatestPriceDate = _stmt.getText(_cursorIndexOfLatestPriceDate)
          }
          val _tmpSourceDbPath: String?
          if (_stmt.isNull(_cursorIndexOfSourceDbPath)) {
            _tmpSourceDbPath = null
          } else {
            _tmpSourceDbPath = _stmt.getText(_cursorIndexOfSourceDbPath)
          }
          val _tmpCompanyCount: Int
          _tmpCompanyCount = _stmt.getLong(_cursorIndexOfCompanyCount).toInt()
          val _tmpScoringRowCount: Int
          _tmpScoringRowCount = _stmt.getLong(_cursorIndexOfScoringRowCount).toInt()
          val _tmpPriceHistoryDays: Int
          _tmpPriceHistoryDays = _stmt.getLong(_cursorIndexOfPriceHistoryDays).toInt()
          _result =
              SnapshotMetadataEntity(_tmpId,_tmpSchemaVersion,_tmpExportedAt,_tmpSnapshotDate,_tmpLatestPriceDate,_tmpSourceDbPath,_tmpCompanyCount,_tmpScoringRowCount,_tmpPriceHistoryDays)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMetadata(): SnapshotMetadataEntity? {
    val _sql: String = "SELECT * FROM snapshot_metadata WHERE id = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfSchemaVersion: Int = getColumnIndexOrThrow(_stmt, "schema_version")
        val _cursorIndexOfExportedAt: Int = getColumnIndexOrThrow(_stmt, "exported_at")
        val _cursorIndexOfSnapshotDate: Int = getColumnIndexOrThrow(_stmt, "snapshot_date")
        val _cursorIndexOfLatestPriceDate: Int = getColumnIndexOrThrow(_stmt, "latest_price_date")
        val _cursorIndexOfSourceDbPath: Int = getColumnIndexOrThrow(_stmt, "source_db_path")
        val _cursorIndexOfCompanyCount: Int = getColumnIndexOrThrow(_stmt, "company_count")
        val _cursorIndexOfScoringRowCount: Int = getColumnIndexOrThrow(_stmt, "scoring_row_count")
        val _cursorIndexOfPriceHistoryDays: Int = getColumnIndexOrThrow(_stmt, "price_history_days")
        val _result: SnapshotMetadataEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpSchemaVersion: Int
          _tmpSchemaVersion = _stmt.getLong(_cursorIndexOfSchemaVersion).toInt()
          val _tmpExportedAt: String
          _tmpExportedAt = _stmt.getText(_cursorIndexOfExportedAt)
          val _tmpSnapshotDate: String?
          if (_stmt.isNull(_cursorIndexOfSnapshotDate)) {
            _tmpSnapshotDate = null
          } else {
            _tmpSnapshotDate = _stmt.getText(_cursorIndexOfSnapshotDate)
          }
          val _tmpLatestPriceDate: String?
          if (_stmt.isNull(_cursorIndexOfLatestPriceDate)) {
            _tmpLatestPriceDate = null
          } else {
            _tmpLatestPriceDate = _stmt.getText(_cursorIndexOfLatestPriceDate)
          }
          val _tmpSourceDbPath: String?
          if (_stmt.isNull(_cursorIndexOfSourceDbPath)) {
            _tmpSourceDbPath = null
          } else {
            _tmpSourceDbPath = _stmt.getText(_cursorIndexOfSourceDbPath)
          }
          val _tmpCompanyCount: Int
          _tmpCompanyCount = _stmt.getLong(_cursorIndexOfCompanyCount).toInt()
          val _tmpScoringRowCount: Int
          _tmpScoringRowCount = _stmt.getLong(_cursorIndexOfScoringRowCount).toInt()
          val _tmpPriceHistoryDays: Int
          _tmpPriceHistoryDays = _stmt.getLong(_cursorIndexOfPriceHistoryDays).toInt()
          _result =
              SnapshotMetadataEntity(_tmpId,_tmpSchemaVersion,_tmpExportedAt,_tmpSnapshotDate,_tmpLatestPriceDate,_tmpSourceDbPath,_tmpCompanyCount,_tmpScoringRowCount,_tmpPriceHistoryDays)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeHomeSummary(): Flow<HomeSummaryEntity?> {
    val _sql: String = "SELECT * FROM home_summary WHERE id = 1"
    return createFlow(__db, false, arrayOf("home_summary")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTotalReturnAvg: Int = getColumnIndexOrThrow(_stmt, "total_return_avg")
        val _cursorIndexOfActiveReturnAvg: Int = getColumnIndexOrThrow(_stmt, "active_return_avg")
        val _cursorIndexOfWinRate: Int = getColumnIndexOrThrow(_stmt, "win_rate")
        val _cursorIndexOfBenchmarkYtd: Int = getColumnIndexOrThrow(_stmt, "benchmark_ytd")
        val _cursorIndexOfMacroDate: Int = getColumnIndexOrThrow(_stmt, "macro_date")
        val _cursorIndexOfPolicyRatePct: Int = getColumnIndexOrThrow(_stmt, "policy_rate_pct")
        val _cursorIndexOfCpiYoyPct: Int = getColumnIndexOrThrow(_stmt, "cpi_yoy_pct")
        val _cursorIndexOfUsdTryRate: Int = getColumnIndexOrThrow(_stmt, "usdtry_rate")
        val _cursorIndexOfRegime: Int = getColumnIndexOrThrow(_stmt, "regime")
        val _cursorIndexOfCashState: Int = getColumnIndexOrThrow(_stmt, "cash_state")
        val _cursorIndexOfCashPct: Int = getColumnIndexOrThrow(_stmt, "cash_pct")
        val _cursorIndexOfCashDaysInState: Int = getColumnIndexOrThrow(_stmt, "cash_days_in_state")
        val _cursorIndexOfCashLastTransitionDate: Int = getColumnIndexOrThrow(_stmt,
            "cash_last_transition_date")
        val _cursorIndexOfCashTargetState: Int = getColumnIndexOrThrow(_stmt, "cash_target_state")
        val _cursorIndexOfCashNotes: Int = getColumnIndexOrThrow(_stmt, "cash_notes")
        val _cursorIndexOfCashRawSignal: Int = getColumnIndexOrThrow(_stmt, "cash_raw_signal")
        val _result: HomeSummaryEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpTotalReturnAvg: Double?
          if (_stmt.isNull(_cursorIndexOfTotalReturnAvg)) {
            _tmpTotalReturnAvg = null
          } else {
            _tmpTotalReturnAvg = _stmt.getDouble(_cursorIndexOfTotalReturnAvg)
          }
          val _tmpActiveReturnAvg: Double?
          if (_stmt.isNull(_cursorIndexOfActiveReturnAvg)) {
            _tmpActiveReturnAvg = null
          } else {
            _tmpActiveReturnAvg = _stmt.getDouble(_cursorIndexOfActiveReturnAvg)
          }
          val _tmpWinRate: Double?
          if (_stmt.isNull(_cursorIndexOfWinRate)) {
            _tmpWinRate = null
          } else {
            _tmpWinRate = _stmt.getDouble(_cursorIndexOfWinRate)
          }
          val _tmpBenchmarkYtd: Double?
          if (_stmt.isNull(_cursorIndexOfBenchmarkYtd)) {
            _tmpBenchmarkYtd = null
          } else {
            _tmpBenchmarkYtd = _stmt.getDouble(_cursorIndexOfBenchmarkYtd)
          }
          val _tmpMacroDate: String?
          if (_stmt.isNull(_cursorIndexOfMacroDate)) {
            _tmpMacroDate = null
          } else {
            _tmpMacroDate = _stmt.getText(_cursorIndexOfMacroDate)
          }
          val _tmpPolicyRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfPolicyRatePct)) {
            _tmpPolicyRatePct = null
          } else {
            _tmpPolicyRatePct = _stmt.getDouble(_cursorIndexOfPolicyRatePct)
          }
          val _tmpCpiYoyPct: Double?
          if (_stmt.isNull(_cursorIndexOfCpiYoyPct)) {
            _tmpCpiYoyPct = null
          } else {
            _tmpCpiYoyPct = _stmt.getDouble(_cursorIndexOfCpiYoyPct)
          }
          val _tmpUsdTryRate: Double?
          if (_stmt.isNull(_cursorIndexOfUsdTryRate)) {
            _tmpUsdTryRate = null
          } else {
            _tmpUsdTryRate = _stmt.getDouble(_cursorIndexOfUsdTryRate)
          }
          val _tmpRegime: String?
          if (_stmt.isNull(_cursorIndexOfRegime)) {
            _tmpRegime = null
          } else {
            _tmpRegime = _stmt.getText(_cursorIndexOfRegime)
          }
          val _tmpCashState: String?
          if (_stmt.isNull(_cursorIndexOfCashState)) {
            _tmpCashState = null
          } else {
            _tmpCashState = _stmt.getText(_cursorIndexOfCashState)
          }
          val _tmpCashPct: Double?
          if (_stmt.isNull(_cursorIndexOfCashPct)) {
            _tmpCashPct = null
          } else {
            _tmpCashPct = _stmt.getDouble(_cursorIndexOfCashPct)
          }
          val _tmpCashDaysInState: Int?
          if (_stmt.isNull(_cursorIndexOfCashDaysInState)) {
            _tmpCashDaysInState = null
          } else {
            _tmpCashDaysInState = _stmt.getLong(_cursorIndexOfCashDaysInState).toInt()
          }
          val _tmpCashLastTransitionDate: String?
          if (_stmt.isNull(_cursorIndexOfCashLastTransitionDate)) {
            _tmpCashLastTransitionDate = null
          } else {
            _tmpCashLastTransitionDate = _stmt.getText(_cursorIndexOfCashLastTransitionDate)
          }
          val _tmpCashTargetState: String?
          if (_stmt.isNull(_cursorIndexOfCashTargetState)) {
            _tmpCashTargetState = null
          } else {
            _tmpCashTargetState = _stmt.getText(_cursorIndexOfCashTargetState)
          }
          val _tmpCashNotes: String?
          if (_stmt.isNull(_cursorIndexOfCashNotes)) {
            _tmpCashNotes = null
          } else {
            _tmpCashNotes = _stmt.getText(_cursorIndexOfCashNotes)
          }
          val _tmpCashRawSignal: Int?
          if (_stmt.isNull(_cursorIndexOfCashRawSignal)) {
            _tmpCashRawSignal = null
          } else {
            _tmpCashRawSignal = _stmt.getLong(_cursorIndexOfCashRawSignal).toInt()
          }
          _result =
              HomeSummaryEntity(_tmpId,_tmpTotalReturnAvg,_tmpActiveReturnAvg,_tmpWinRate,_tmpBenchmarkYtd,_tmpMacroDate,_tmpPolicyRatePct,_tmpCpiYoyPct,_tmpUsdTryRate,_tmpRegime,_tmpCashState,_tmpCashPct,_tmpCashDaysInState,_tmpCashLastTransitionDate,_tmpCashTargetState,_tmpCashNotes,_tmpCashRawSignal)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeOpenPositions(): Flow<List<OpenPositionEntity>> {
    val _sql: String = "SELECT * FROM open_positions ORDER BY sort_order"
    return createFlow(__db, false, arrayOf("open_positions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sort_order")
        val _cursorIndexOfPortfolio: Int = getColumnIndexOrThrow(_stmt, "portfolio")
        val _cursorIndexOfTicker: Int = getColumnIndexOrThrow(_stmt, "ticker")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfCompanyId: Int = getColumnIndexOrThrow(_stmt, "company_id")
        val _cursorIndexOfEntryPrice: Int = getColumnIndexOrThrow(_stmt, "entry_price")
        val _cursorIndexOfCurrentPrice: Int = getColumnIndexOrThrow(_stmt, "current_price")
        val _cursorIndexOfPnlPct: Int = getColumnIndexOrThrow(_stmt, "pnl_pct")
        val _cursorIndexOfTargetPrice: Int = getColumnIndexOrThrow(_stmt, "target_price")
        val _cursorIndexOfStopLossPrice: Int = getColumnIndexOrThrow(_stmt, "stop_loss_price")
        val _cursorIndexOfStopPctFromEntry: Int = getColumnIndexOrThrow(_stmt,
            "stop_pct_from_entry")
        val _cursorIndexOfCompositeScore: Int = getColumnIndexOrThrow(_stmt, "composite_score")
        val _cursorIndexOfSelectionDate: Int = getColumnIndexOrThrow(_stmt, "selection_date")
        val _cursorIndexOfDaysHeld: Int = getColumnIndexOrThrow(_stmt, "days_held")
        val _cursorIndexOfReasonTopFactorsJson: Int = getColumnIndexOrThrow(_stmt,
            "reason_top_factors_json")
        val _cursorIndexOfQualityFlagsJson: Int = getColumnIndexOrThrow(_stmt, "quality_flags_json")
        val _cursorIndexOfDcfMosPct: Int = getColumnIndexOrThrow(_stmt, "dcf_margin_of_safety_pct")
        val _cursorIndexOfDcfIntrinsicValue: Int = getColumnIndexOrThrow(_stmt,
            "dcf_intrinsic_value")
        val _cursorIndexOfDcfGrowthRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_growth_rate_pct")
        val _cursorIndexOfDcfDiscountRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_discount_rate_pct")
        val _cursorIndexOfDcfTerminalGrowthPct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_terminal_growth_pct")
        val _result: MutableList<OpenPositionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: OpenPositionEntity
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_cursorIndexOfSortOrder).toInt()
          val _tmpPortfolio: String?
          if (_stmt.isNull(_cursorIndexOfPortfolio)) {
            _tmpPortfolio = null
          } else {
            _tmpPortfolio = _stmt.getText(_cursorIndexOfPortfolio)
          }
          val _tmpTicker: String
          _tmpTicker = _stmt.getText(_cursorIndexOfTicker)
          val _tmpName: String?
          if (_stmt.isNull(_cursorIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_cursorIndexOfName)
          }
          val _tmpCompanyId: Int?
          if (_stmt.isNull(_cursorIndexOfCompanyId)) {
            _tmpCompanyId = null
          } else {
            _tmpCompanyId = _stmt.getLong(_cursorIndexOfCompanyId).toInt()
          }
          val _tmpEntryPrice: Double?
          if (_stmt.isNull(_cursorIndexOfEntryPrice)) {
            _tmpEntryPrice = null
          } else {
            _tmpEntryPrice = _stmt.getDouble(_cursorIndexOfEntryPrice)
          }
          val _tmpCurrentPrice: Double?
          if (_stmt.isNull(_cursorIndexOfCurrentPrice)) {
            _tmpCurrentPrice = null
          } else {
            _tmpCurrentPrice = _stmt.getDouble(_cursorIndexOfCurrentPrice)
          }
          val _tmpPnlPct: Double?
          if (_stmt.isNull(_cursorIndexOfPnlPct)) {
            _tmpPnlPct = null
          } else {
            _tmpPnlPct = _stmt.getDouble(_cursorIndexOfPnlPct)
          }
          val _tmpTargetPrice: Double?
          if (_stmt.isNull(_cursorIndexOfTargetPrice)) {
            _tmpTargetPrice = null
          } else {
            _tmpTargetPrice = _stmt.getDouble(_cursorIndexOfTargetPrice)
          }
          val _tmpStopLossPrice: Double?
          if (_stmt.isNull(_cursorIndexOfStopLossPrice)) {
            _tmpStopLossPrice = null
          } else {
            _tmpStopLossPrice = _stmt.getDouble(_cursorIndexOfStopLossPrice)
          }
          val _tmpStopPctFromEntry: Double?
          if (_stmt.isNull(_cursorIndexOfStopPctFromEntry)) {
            _tmpStopPctFromEntry = null
          } else {
            _tmpStopPctFromEntry = _stmt.getDouble(_cursorIndexOfStopPctFromEntry)
          }
          val _tmpCompositeScore: Double?
          if (_stmt.isNull(_cursorIndexOfCompositeScore)) {
            _tmpCompositeScore = null
          } else {
            _tmpCompositeScore = _stmt.getDouble(_cursorIndexOfCompositeScore)
          }
          val _tmpSelectionDate: String?
          if (_stmt.isNull(_cursorIndexOfSelectionDate)) {
            _tmpSelectionDate = null
          } else {
            _tmpSelectionDate = _stmt.getText(_cursorIndexOfSelectionDate)
          }
          val _tmpDaysHeld: Int?
          if (_stmt.isNull(_cursorIndexOfDaysHeld)) {
            _tmpDaysHeld = null
          } else {
            _tmpDaysHeld = _stmt.getLong(_cursorIndexOfDaysHeld).toInt()
          }
          val _tmpReasonTopFactorsJson: String?
          if (_stmt.isNull(_cursorIndexOfReasonTopFactorsJson)) {
            _tmpReasonTopFactorsJson = null
          } else {
            _tmpReasonTopFactorsJson = _stmt.getText(_cursorIndexOfReasonTopFactorsJson)
          }
          val _tmpQualityFlagsJson: String?
          if (_stmt.isNull(_cursorIndexOfQualityFlagsJson)) {
            _tmpQualityFlagsJson = null
          } else {
            _tmpQualityFlagsJson = _stmt.getText(_cursorIndexOfQualityFlagsJson)
          }
          val _tmpDcfMosPct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfMosPct)) {
            _tmpDcfMosPct = null
          } else {
            _tmpDcfMosPct = _stmt.getDouble(_cursorIndexOfDcfMosPct)
          }
          val _tmpDcfIntrinsicValue: Double?
          if (_stmt.isNull(_cursorIndexOfDcfIntrinsicValue)) {
            _tmpDcfIntrinsicValue = null
          } else {
            _tmpDcfIntrinsicValue = _stmt.getDouble(_cursorIndexOfDcfIntrinsicValue)
          }
          val _tmpDcfGrowthRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfGrowthRatePct)) {
            _tmpDcfGrowthRatePct = null
          } else {
            _tmpDcfGrowthRatePct = _stmt.getDouble(_cursorIndexOfDcfGrowthRatePct)
          }
          val _tmpDcfDiscountRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfDiscountRatePct)) {
            _tmpDcfDiscountRatePct = null
          } else {
            _tmpDcfDiscountRatePct = _stmt.getDouble(_cursorIndexOfDcfDiscountRatePct)
          }
          val _tmpDcfTerminalGrowthPct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfTerminalGrowthPct)) {
            _tmpDcfTerminalGrowthPct = null
          } else {
            _tmpDcfTerminalGrowthPct = _stmt.getDouble(_cursorIndexOfDcfTerminalGrowthPct)
          }
          _item =
              OpenPositionEntity(_tmpSortOrder,_tmpPortfolio,_tmpTicker,_tmpName,_tmpCompanyId,_tmpEntryPrice,_tmpCurrentPrice,_tmpPnlPct,_tmpTargetPrice,_tmpStopLossPrice,_tmpStopPctFromEntry,_tmpCompositeScore,_tmpSelectionDate,_tmpDaysHeld,_tmpReasonTopFactorsJson,_tmpQualityFlagsJson,_tmpDcfMosPct,_tmpDcfIntrinsicValue,_tmpDcfGrowthRatePct,_tmpDcfDiscountRatePct,_tmpDcfTerminalGrowthPct)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observePortfolioHistory(): Flow<List<PortfolioHistoryEntity>> {
    val _sql: String = "SELECT * FROM portfolio_history ORDER BY exit_date DESC, sort_order"
    return createFlow(__db, false, arrayOf("portfolio_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sort_order")
        val _cursorIndexOfPortfolio: Int = getColumnIndexOrThrow(_stmt, "portfolio")
        val _cursorIndexOfTicker: Int = getColumnIndexOrThrow(_stmt, "ticker")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfSelectionDate: Int = getColumnIndexOrThrow(_stmt, "selection_date")
        val _cursorIndexOfExitDate: Int = getColumnIndexOrThrow(_stmt, "exit_date")
        val _cursorIndexOfEntryPrice: Int = getColumnIndexOrThrow(_stmt, "entry_price")
        val _cursorIndexOfExitPrice: Int = getColumnIndexOrThrow(_stmt, "exit_price")
        val _cursorIndexOfPnlPct: Int = getColumnIndexOrThrow(_stmt, "pnl_pct")
        val _cursorIndexOfExitReason: Int = getColumnIndexOrThrow(_stmt, "exit_reason")
        val _cursorIndexOfHoldingDays: Int = getColumnIndexOrThrow(_stmt, "holding_days")
        val _result: MutableList<PortfolioHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PortfolioHistoryEntity
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_cursorIndexOfSortOrder).toInt()
          val _tmpPortfolio: String?
          if (_stmt.isNull(_cursorIndexOfPortfolio)) {
            _tmpPortfolio = null
          } else {
            _tmpPortfolio = _stmt.getText(_cursorIndexOfPortfolio)
          }
          val _tmpTicker: String
          _tmpTicker = _stmt.getText(_cursorIndexOfTicker)
          val _tmpName: String?
          if (_stmt.isNull(_cursorIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_cursorIndexOfName)
          }
          val _tmpSelectionDate: String?
          if (_stmt.isNull(_cursorIndexOfSelectionDate)) {
            _tmpSelectionDate = null
          } else {
            _tmpSelectionDate = _stmt.getText(_cursorIndexOfSelectionDate)
          }
          val _tmpExitDate: String?
          if (_stmt.isNull(_cursorIndexOfExitDate)) {
            _tmpExitDate = null
          } else {
            _tmpExitDate = _stmt.getText(_cursorIndexOfExitDate)
          }
          val _tmpEntryPrice: Double?
          if (_stmt.isNull(_cursorIndexOfEntryPrice)) {
            _tmpEntryPrice = null
          } else {
            _tmpEntryPrice = _stmt.getDouble(_cursorIndexOfEntryPrice)
          }
          val _tmpExitPrice: Double?
          if (_stmt.isNull(_cursorIndexOfExitPrice)) {
            _tmpExitPrice = null
          } else {
            _tmpExitPrice = _stmt.getDouble(_cursorIndexOfExitPrice)
          }
          val _tmpPnlPct: Double?
          if (_stmt.isNull(_cursorIndexOfPnlPct)) {
            _tmpPnlPct = null
          } else {
            _tmpPnlPct = _stmt.getDouble(_cursorIndexOfPnlPct)
          }
          val _tmpExitReason: String?
          if (_stmt.isNull(_cursorIndexOfExitReason)) {
            _tmpExitReason = null
          } else {
            _tmpExitReason = _stmt.getText(_cursorIndexOfExitReason)
          }
          val _tmpHoldingDays: Int?
          if (_stmt.isNull(_cursorIndexOfHoldingDays)) {
            _tmpHoldingDays = null
          } else {
            _tmpHoldingDays = _stmt.getLong(_cursorIndexOfHoldingDays).toInt()
          }
          _item =
              PortfolioHistoryEntity(_tmpSortOrder,_tmpPortfolio,_tmpTicker,_tmpName,_tmpSelectionDate,_tmpExitDate,_tmpEntryPrice,_tmpExitPrice,_tmpPnlPct,_tmpExitReason,_tmpHoldingDays)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTopScoring(limit: Int): Flow<List<ScoringLatestEntity>> {
    val _sql: String =
        "SELECT * FROM scoring_latest WHERE alpha_core_eligible = 1 ORDER BY ranking_score DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("scoring_latest")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _cursorIndexOfCompanyId: Int = getColumnIndexOrThrow(_stmt, "company_id")
        val _cursorIndexOfTicker: Int = getColumnIndexOrThrow(_stmt, "ticker")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfSector: Int = getColumnIndexOrThrow(_stmt, "sector")
        val _cursorIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _cursorIndexOfIsBist100: Int = getColumnIndexOrThrow(_stmt, "is_bist100")
        val _cursorIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "is_active")
        val _cursorIndexOfFreeFloatPct: Int = getColumnIndexOrThrow(_stmt, "free_float_pct")
        val _cursorIndexOfAvgVolumeTry: Int = getColumnIndexOrThrow(_stmt, "avg_volume_try")
        val _cursorIndexOfRankingScore: Int = getColumnIndexOrThrow(_stmt, "ranking_score")
        val _cursorIndexOfRankingSource: Int = getColumnIndexOrThrow(_stmt, "ranking_source")
        val _cursorIndexOfModelScore: Int = getColumnIndexOrThrow(_stmt, "model_score")
        val _cursorIndexOfAlpha: Int = getColumnIndexOrThrow(_stmt, "alpha")
        val _cursorIndexOfAlphaXScore: Int = getColumnIndexOrThrow(_stmt, "alpha_x_score")
        val _cursorIndexOfAlphaXRank: Int = getColumnIndexOrThrow(_stmt, "alpha_x_rank")
        val _cursorIndexOfAlphaXConfidence: Int = getColumnIndexOrThrow(_stmt, "alpha_x_confidence")
        val _cursorIndexOfAlphaCoreEligible: Int = getColumnIndexOrThrow(_stmt,
            "alpha_core_eligible")
        val _cursorIndexOfAlphaXEligible: Int = getColumnIndexOrThrow(_stmt, "alpha_x_eligible")
        val _cursorIndexOfAlphaReason: Int = getColumnIndexOrThrow(_stmt, "alpha_reason")
        val _cursorIndexOfAlphaPrimaryBlocker: Int = getColumnIndexOrThrow(_stmt,
            "alpha_primary_blocker")
        val _cursorIndexOfAlphaResearchBucket: Int = getColumnIndexOrThrow(_stmt,
            "alpha_research_bucket")
        val _cursorIndexOfAlphaSnapshotStreak: Int = getColumnIndexOrThrow(_stmt,
            "alpha_snapshot_streak")
        val _cursorIndexOfAiInsight: Int = getColumnIndexOrThrow(_stmt, "ai_insight")
        val _cursorIndexOfRisk: Int = getColumnIndexOrThrow(_stmt, "risk")
        val _cursorIndexOfDataCompleteness: Int = getColumnIndexOrThrow(_stmt, "data_completeness")
        val _cursorIndexOfScoringDate: Int = getColumnIndexOrThrow(_stmt, "scoring_date")
        val _cursorIndexOfModelUsed: Int = getColumnIndexOrThrow(_stmt, "model_used")
        val _cursorIndexOfBuffett: Int = getColumnIndexOrThrow(_stmt, "buffett")
        val _cursorIndexOfGraham: Int = getColumnIndexOrThrow(_stmt, "graham")
        val _cursorIndexOfPiotroski: Int = getColumnIndexOrThrow(_stmt, "piotroski")
        val _cursorIndexOfPiotroskiRaw: Int = getColumnIndexOrThrow(_stmt, "piotroski_raw")
        val _cursorIndexOfMagicFormula: Int = getColumnIndexOrThrow(_stmt, "magic_formula")
        val _cursorIndexOfLynchPeg: Int = getColumnIndexOrThrow(_stmt, "lynch_peg")
        val _cursorIndexOfDcfMos: Int = getColumnIndexOrThrow(_stmt, "dcf_mos")
        val _cursorIndexOfMomentum: Int = getColumnIndexOrThrow(_stmt, "momentum")
        val _cursorIndexOfInsider: Int = getColumnIndexOrThrow(_stmt, "insider")
        val _cursorIndexOfTechnical: Int = getColumnIndexOrThrow(_stmt, "technical")
        val _cursorIndexOfDividend: Int = getColumnIndexOrThrow(_stmt, "dividend")
        val _cursorIndexOfBeta: Int = getColumnIndexOrThrow(_stmt, "beta")
        val _cursorIndexOfDelta: Int = getColumnIndexOrThrow(_stmt, "delta")
        val _cursorIndexOfQualityFlagsJson: Int = getColumnIndexOrThrow(_stmt, "quality_flags_json")
        val _cursorIndexOfDcfIntrinsicValue: Int = getColumnIndexOrThrow(_stmt,
            "dcf_intrinsic_value")
        val _cursorIndexOfDcfGrowthRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_growth_rate_pct")
        val _cursorIndexOfDcfDiscountRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_discount_rate_pct")
        val _cursorIndexOfDcfTerminalGrowthPct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_terminal_growth_pct")
        val _result: MutableList<ScoringLatestEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ScoringLatestEntity
          val _tmpCompanyId: Int
          _tmpCompanyId = _stmt.getLong(_cursorIndexOfCompanyId).toInt()
          val _tmpTicker: String
          _tmpTicker = _stmt.getText(_cursorIndexOfTicker)
          val _tmpName: String?
          if (_stmt.isNull(_cursorIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_cursorIndexOfName)
          }
          val _tmpSector: String?
          if (_stmt.isNull(_cursorIndexOfSector)) {
            _tmpSector = null
          } else {
            _tmpSector = _stmt.getText(_cursorIndexOfSector)
          }
          val _tmpType: String?
          if (_stmt.isNull(_cursorIndexOfType)) {
            _tmpType = null
          } else {
            _tmpType = _stmt.getText(_cursorIndexOfType)
          }
          val _tmpIsBist100: Int
          _tmpIsBist100 = _stmt.getLong(_cursorIndexOfIsBist100).toInt()
          val _tmpIsActive: Int
          _tmpIsActive = _stmt.getLong(_cursorIndexOfIsActive).toInt()
          val _tmpFreeFloatPct: Double?
          if (_stmt.isNull(_cursorIndexOfFreeFloatPct)) {
            _tmpFreeFloatPct = null
          } else {
            _tmpFreeFloatPct = _stmt.getDouble(_cursorIndexOfFreeFloatPct)
          }
          val _tmpAvgVolumeTry: Double?
          if (_stmt.isNull(_cursorIndexOfAvgVolumeTry)) {
            _tmpAvgVolumeTry = null
          } else {
            _tmpAvgVolumeTry = _stmt.getDouble(_cursorIndexOfAvgVolumeTry)
          }
          val _tmpRankingScore: Double?
          if (_stmt.isNull(_cursorIndexOfRankingScore)) {
            _tmpRankingScore = null
          } else {
            _tmpRankingScore = _stmt.getDouble(_cursorIndexOfRankingScore)
          }
          val _tmpRankingSource: String?
          if (_stmt.isNull(_cursorIndexOfRankingSource)) {
            _tmpRankingSource = null
          } else {
            _tmpRankingSource = _stmt.getText(_cursorIndexOfRankingSource)
          }
          val _tmpModelScore: Double?
          if (_stmt.isNull(_cursorIndexOfModelScore)) {
            _tmpModelScore = null
          } else {
            _tmpModelScore = _stmt.getDouble(_cursorIndexOfModelScore)
          }
          val _tmpAlpha: Double?
          if (_stmt.isNull(_cursorIndexOfAlpha)) {
            _tmpAlpha = null
          } else {
            _tmpAlpha = _stmt.getDouble(_cursorIndexOfAlpha)
          }
          val _tmpAlphaXScore: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXScore)) {
            _tmpAlphaXScore = null
          } else {
            _tmpAlphaXScore = _stmt.getDouble(_cursorIndexOfAlphaXScore)
          }
          val _tmpAlphaXRank: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXRank)) {
            _tmpAlphaXRank = null
          } else {
            _tmpAlphaXRank = _stmt.getDouble(_cursorIndexOfAlphaXRank)
          }
          val _tmpAlphaXConfidence: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXConfidence)) {
            _tmpAlphaXConfidence = null
          } else {
            _tmpAlphaXConfidence = _stmt.getDouble(_cursorIndexOfAlphaXConfidence)
          }
          val _tmpAlphaCoreEligible: Int
          _tmpAlphaCoreEligible = _stmt.getLong(_cursorIndexOfAlphaCoreEligible).toInt()
          val _tmpAlphaXEligible: Int
          _tmpAlphaXEligible = _stmt.getLong(_cursorIndexOfAlphaXEligible).toInt()
          val _tmpAlphaReason: String?
          if (_stmt.isNull(_cursorIndexOfAlphaReason)) {
            _tmpAlphaReason = null
          } else {
            _tmpAlphaReason = _stmt.getText(_cursorIndexOfAlphaReason)
          }
          val _tmpAlphaPrimaryBlocker: String?
          if (_stmt.isNull(_cursorIndexOfAlphaPrimaryBlocker)) {
            _tmpAlphaPrimaryBlocker = null
          } else {
            _tmpAlphaPrimaryBlocker = _stmt.getText(_cursorIndexOfAlphaPrimaryBlocker)
          }
          val _tmpAlphaResearchBucket: String?
          if (_stmt.isNull(_cursorIndexOfAlphaResearchBucket)) {
            _tmpAlphaResearchBucket = null
          } else {
            _tmpAlphaResearchBucket = _stmt.getText(_cursorIndexOfAlphaResearchBucket)
          }
          val _tmpAlphaSnapshotStreak: Int?
          if (_stmt.isNull(_cursorIndexOfAlphaSnapshotStreak)) {
            _tmpAlphaSnapshotStreak = null
          } else {
            _tmpAlphaSnapshotStreak = _stmt.getLong(_cursorIndexOfAlphaSnapshotStreak).toInt()
          }
          val _tmpAiInsight: String?
          if (_stmt.isNull(_cursorIndexOfAiInsight)) {
            _tmpAiInsight = null
          } else {
            _tmpAiInsight = _stmt.getText(_cursorIndexOfAiInsight)
          }
          val _tmpRisk: String?
          if (_stmt.isNull(_cursorIndexOfRisk)) {
            _tmpRisk = null
          } else {
            _tmpRisk = _stmt.getText(_cursorIndexOfRisk)
          }
          val _tmpDataCompleteness: Double?
          if (_stmt.isNull(_cursorIndexOfDataCompleteness)) {
            _tmpDataCompleteness = null
          } else {
            _tmpDataCompleteness = _stmt.getDouble(_cursorIndexOfDataCompleteness)
          }
          val _tmpScoringDate: String?
          if (_stmt.isNull(_cursorIndexOfScoringDate)) {
            _tmpScoringDate = null
          } else {
            _tmpScoringDate = _stmt.getText(_cursorIndexOfScoringDate)
          }
          val _tmpModelUsed: String?
          if (_stmt.isNull(_cursorIndexOfModelUsed)) {
            _tmpModelUsed = null
          } else {
            _tmpModelUsed = _stmt.getText(_cursorIndexOfModelUsed)
          }
          val _tmpBuffett: Double?
          if (_stmt.isNull(_cursorIndexOfBuffett)) {
            _tmpBuffett = null
          } else {
            _tmpBuffett = _stmt.getDouble(_cursorIndexOfBuffett)
          }
          val _tmpGraham: Double?
          if (_stmt.isNull(_cursorIndexOfGraham)) {
            _tmpGraham = null
          } else {
            _tmpGraham = _stmt.getDouble(_cursorIndexOfGraham)
          }
          val _tmpPiotroski: Double?
          if (_stmt.isNull(_cursorIndexOfPiotroski)) {
            _tmpPiotroski = null
          } else {
            _tmpPiotroski = _stmt.getDouble(_cursorIndexOfPiotroski)
          }
          val _tmpPiotroskiRaw: Int?
          if (_stmt.isNull(_cursorIndexOfPiotroskiRaw)) {
            _tmpPiotroskiRaw = null
          } else {
            _tmpPiotroskiRaw = _stmt.getLong(_cursorIndexOfPiotroskiRaw).toInt()
          }
          val _tmpMagicFormula: Double?
          if (_stmt.isNull(_cursorIndexOfMagicFormula)) {
            _tmpMagicFormula = null
          } else {
            _tmpMagicFormula = _stmt.getDouble(_cursorIndexOfMagicFormula)
          }
          val _tmpLynchPeg: Double?
          if (_stmt.isNull(_cursorIndexOfLynchPeg)) {
            _tmpLynchPeg = null
          } else {
            _tmpLynchPeg = _stmt.getDouble(_cursorIndexOfLynchPeg)
          }
          val _tmpDcfMos: Double?
          if (_stmt.isNull(_cursorIndexOfDcfMos)) {
            _tmpDcfMos = null
          } else {
            _tmpDcfMos = _stmt.getDouble(_cursorIndexOfDcfMos)
          }
          val _tmpMomentum: Double?
          if (_stmt.isNull(_cursorIndexOfMomentum)) {
            _tmpMomentum = null
          } else {
            _tmpMomentum = _stmt.getDouble(_cursorIndexOfMomentum)
          }
          val _tmpInsider: Double?
          if (_stmt.isNull(_cursorIndexOfInsider)) {
            _tmpInsider = null
          } else {
            _tmpInsider = _stmt.getDouble(_cursorIndexOfInsider)
          }
          val _tmpTechnical: Double?
          if (_stmt.isNull(_cursorIndexOfTechnical)) {
            _tmpTechnical = null
          } else {
            _tmpTechnical = _stmt.getDouble(_cursorIndexOfTechnical)
          }
          val _tmpDividend: Double?
          if (_stmt.isNull(_cursorIndexOfDividend)) {
            _tmpDividend = null
          } else {
            _tmpDividend = _stmt.getDouble(_cursorIndexOfDividend)
          }
          val _tmpBeta: Double?
          if (_stmt.isNull(_cursorIndexOfBeta)) {
            _tmpBeta = null
          } else {
            _tmpBeta = _stmt.getDouble(_cursorIndexOfBeta)
          }
          val _tmpDelta: Double?
          if (_stmt.isNull(_cursorIndexOfDelta)) {
            _tmpDelta = null
          } else {
            _tmpDelta = _stmt.getDouble(_cursorIndexOfDelta)
          }
          val _tmpQualityFlagsJson: String?
          if (_stmt.isNull(_cursorIndexOfQualityFlagsJson)) {
            _tmpQualityFlagsJson = null
          } else {
            _tmpQualityFlagsJson = _stmt.getText(_cursorIndexOfQualityFlagsJson)
          }
          val _tmpDcfIntrinsicValue: Double?
          if (_stmt.isNull(_cursorIndexOfDcfIntrinsicValue)) {
            _tmpDcfIntrinsicValue = null
          } else {
            _tmpDcfIntrinsicValue = _stmt.getDouble(_cursorIndexOfDcfIntrinsicValue)
          }
          val _tmpDcfGrowthRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfGrowthRatePct)) {
            _tmpDcfGrowthRatePct = null
          } else {
            _tmpDcfGrowthRatePct = _stmt.getDouble(_cursorIndexOfDcfGrowthRatePct)
          }
          val _tmpDcfDiscountRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfDiscountRatePct)) {
            _tmpDcfDiscountRatePct = null
          } else {
            _tmpDcfDiscountRatePct = _stmt.getDouble(_cursorIndexOfDcfDiscountRatePct)
          }
          val _tmpDcfTerminalGrowthPct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfTerminalGrowthPct)) {
            _tmpDcfTerminalGrowthPct = null
          } else {
            _tmpDcfTerminalGrowthPct = _stmt.getDouble(_cursorIndexOfDcfTerminalGrowthPct)
          }
          _item =
              ScoringLatestEntity(_tmpCompanyId,_tmpTicker,_tmpName,_tmpSector,_tmpType,_tmpIsBist100,_tmpIsActive,_tmpFreeFloatPct,_tmpAvgVolumeTry,_tmpRankingScore,_tmpRankingSource,_tmpModelScore,_tmpAlpha,_tmpAlphaXScore,_tmpAlphaXRank,_tmpAlphaXConfidence,_tmpAlphaCoreEligible,_tmpAlphaXEligible,_tmpAlphaReason,_tmpAlphaPrimaryBlocker,_tmpAlphaResearchBucket,_tmpAlphaSnapshotStreak,_tmpAiInsight,_tmpRisk,_tmpDataCompleteness,_tmpScoringDate,_tmpModelUsed,_tmpBuffett,_tmpGraham,_tmpPiotroski,_tmpPiotroskiRaw,_tmpMagicFormula,_tmpLynchPeg,_tmpDcfMos,_tmpMomentum,_tmpInsider,_tmpTechnical,_tmpDividend,_tmpBeta,_tmpDelta,_tmpQualityFlagsJson,_tmpDcfIntrinsicValue,_tmpDcfGrowthRatePct,_tmpDcfDiscountRatePct,_tmpDcfTerminalGrowthPct)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun queryScoring(
    onlyAlphaCore: Int,
    onlyAlphaX: Int,
    onlyBist100: Int,
    sector: String?,
    risk: String?,
    minScore: Double?,
    search: String?,
    limit: Int,
    offset: Int,
  ): List<ScoringLatestEntity> {
    val _sql: String = """
        |
        |        SELECT * FROM scoring_latest 
        |        WHERE (? = 0 OR alpha_core_eligible = 1)
        |          AND (? = 0 OR alpha_x_eligible = 1)
        |          AND (? = 0 OR is_bist100 = 1)
        |          AND (? IS NULL OR sector = ?)
        |          AND (? IS NULL OR risk = ?)
        |          AND (? IS NULL OR ranking_score >= ?)
        |          AND (? IS NULL OR ticker LIKE '%' || ? || '%' OR name LIKE '%' || ? || '%')
        |        ORDER BY ranking_score DESC 
        |        LIMIT ? OFFSET ?
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, onlyAlphaCore.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, onlyAlphaX.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, onlyBist100.toLong())
        _argIndex = 4
        if (sector == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, sector)
        }
        _argIndex = 5
        if (sector == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, sector)
        }
        _argIndex = 6
        if (risk == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, risk)
        }
        _argIndex = 7
        if (risk == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, risk)
        }
        _argIndex = 8
        if (minScore == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindDouble(_argIndex, minScore)
        }
        _argIndex = 9
        if (minScore == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindDouble(_argIndex, minScore)
        }
        _argIndex = 10
        if (search == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, search)
        }
        _argIndex = 11
        if (search == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, search)
        }
        _argIndex = 12
        if (search == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, search)
        }
        _argIndex = 13
        _stmt.bindLong(_argIndex, limit.toLong())
        _argIndex = 14
        _stmt.bindLong(_argIndex, offset.toLong())
        val _cursorIndexOfCompanyId: Int = getColumnIndexOrThrow(_stmt, "company_id")
        val _cursorIndexOfTicker: Int = getColumnIndexOrThrow(_stmt, "ticker")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfSector: Int = getColumnIndexOrThrow(_stmt, "sector")
        val _cursorIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _cursorIndexOfIsBist100: Int = getColumnIndexOrThrow(_stmt, "is_bist100")
        val _cursorIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "is_active")
        val _cursorIndexOfFreeFloatPct: Int = getColumnIndexOrThrow(_stmt, "free_float_pct")
        val _cursorIndexOfAvgVolumeTry: Int = getColumnIndexOrThrow(_stmt, "avg_volume_try")
        val _cursorIndexOfRankingScore: Int = getColumnIndexOrThrow(_stmt, "ranking_score")
        val _cursorIndexOfRankingSource: Int = getColumnIndexOrThrow(_stmt, "ranking_source")
        val _cursorIndexOfModelScore: Int = getColumnIndexOrThrow(_stmt, "model_score")
        val _cursorIndexOfAlpha: Int = getColumnIndexOrThrow(_stmt, "alpha")
        val _cursorIndexOfAlphaXScore: Int = getColumnIndexOrThrow(_stmt, "alpha_x_score")
        val _cursorIndexOfAlphaXRank: Int = getColumnIndexOrThrow(_stmt, "alpha_x_rank")
        val _cursorIndexOfAlphaXConfidence: Int = getColumnIndexOrThrow(_stmt, "alpha_x_confidence")
        val _cursorIndexOfAlphaCoreEligible: Int = getColumnIndexOrThrow(_stmt,
            "alpha_core_eligible")
        val _cursorIndexOfAlphaXEligible: Int = getColumnIndexOrThrow(_stmt, "alpha_x_eligible")
        val _cursorIndexOfAlphaReason: Int = getColumnIndexOrThrow(_stmt, "alpha_reason")
        val _cursorIndexOfAlphaPrimaryBlocker: Int = getColumnIndexOrThrow(_stmt,
            "alpha_primary_blocker")
        val _cursorIndexOfAlphaResearchBucket: Int = getColumnIndexOrThrow(_stmt,
            "alpha_research_bucket")
        val _cursorIndexOfAlphaSnapshotStreak: Int = getColumnIndexOrThrow(_stmt,
            "alpha_snapshot_streak")
        val _cursorIndexOfAiInsight: Int = getColumnIndexOrThrow(_stmt, "ai_insight")
        val _cursorIndexOfRisk: Int = getColumnIndexOrThrow(_stmt, "risk")
        val _cursorIndexOfDataCompleteness: Int = getColumnIndexOrThrow(_stmt, "data_completeness")
        val _cursorIndexOfScoringDate: Int = getColumnIndexOrThrow(_stmt, "scoring_date")
        val _cursorIndexOfModelUsed: Int = getColumnIndexOrThrow(_stmt, "model_used")
        val _cursorIndexOfBuffett: Int = getColumnIndexOrThrow(_stmt, "buffett")
        val _cursorIndexOfGraham: Int = getColumnIndexOrThrow(_stmt, "graham")
        val _cursorIndexOfPiotroski: Int = getColumnIndexOrThrow(_stmt, "piotroski")
        val _cursorIndexOfPiotroskiRaw: Int = getColumnIndexOrThrow(_stmt, "piotroski_raw")
        val _cursorIndexOfMagicFormula: Int = getColumnIndexOrThrow(_stmt, "magic_formula")
        val _cursorIndexOfLynchPeg: Int = getColumnIndexOrThrow(_stmt, "lynch_peg")
        val _cursorIndexOfDcfMos: Int = getColumnIndexOrThrow(_stmt, "dcf_mos")
        val _cursorIndexOfMomentum: Int = getColumnIndexOrThrow(_stmt, "momentum")
        val _cursorIndexOfInsider: Int = getColumnIndexOrThrow(_stmt, "insider")
        val _cursorIndexOfTechnical: Int = getColumnIndexOrThrow(_stmt, "technical")
        val _cursorIndexOfDividend: Int = getColumnIndexOrThrow(_stmt, "dividend")
        val _cursorIndexOfBeta: Int = getColumnIndexOrThrow(_stmt, "beta")
        val _cursorIndexOfDelta: Int = getColumnIndexOrThrow(_stmt, "delta")
        val _cursorIndexOfQualityFlagsJson: Int = getColumnIndexOrThrow(_stmt, "quality_flags_json")
        val _cursorIndexOfDcfIntrinsicValue: Int = getColumnIndexOrThrow(_stmt,
            "dcf_intrinsic_value")
        val _cursorIndexOfDcfGrowthRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_growth_rate_pct")
        val _cursorIndexOfDcfDiscountRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_discount_rate_pct")
        val _cursorIndexOfDcfTerminalGrowthPct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_terminal_growth_pct")
        val _result: MutableList<ScoringLatestEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ScoringLatestEntity
          val _tmpCompanyId: Int
          _tmpCompanyId = _stmt.getLong(_cursorIndexOfCompanyId).toInt()
          val _tmpTicker: String
          _tmpTicker = _stmt.getText(_cursorIndexOfTicker)
          val _tmpName: String?
          if (_stmt.isNull(_cursorIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_cursorIndexOfName)
          }
          val _tmpSector: String?
          if (_stmt.isNull(_cursorIndexOfSector)) {
            _tmpSector = null
          } else {
            _tmpSector = _stmt.getText(_cursorIndexOfSector)
          }
          val _tmpType: String?
          if (_stmt.isNull(_cursorIndexOfType)) {
            _tmpType = null
          } else {
            _tmpType = _stmt.getText(_cursorIndexOfType)
          }
          val _tmpIsBist100: Int
          _tmpIsBist100 = _stmt.getLong(_cursorIndexOfIsBist100).toInt()
          val _tmpIsActive: Int
          _tmpIsActive = _stmt.getLong(_cursorIndexOfIsActive).toInt()
          val _tmpFreeFloatPct: Double?
          if (_stmt.isNull(_cursorIndexOfFreeFloatPct)) {
            _tmpFreeFloatPct = null
          } else {
            _tmpFreeFloatPct = _stmt.getDouble(_cursorIndexOfFreeFloatPct)
          }
          val _tmpAvgVolumeTry: Double?
          if (_stmt.isNull(_cursorIndexOfAvgVolumeTry)) {
            _tmpAvgVolumeTry = null
          } else {
            _tmpAvgVolumeTry = _stmt.getDouble(_cursorIndexOfAvgVolumeTry)
          }
          val _tmpRankingScore: Double?
          if (_stmt.isNull(_cursorIndexOfRankingScore)) {
            _tmpRankingScore = null
          } else {
            _tmpRankingScore = _stmt.getDouble(_cursorIndexOfRankingScore)
          }
          val _tmpRankingSource: String?
          if (_stmt.isNull(_cursorIndexOfRankingSource)) {
            _tmpRankingSource = null
          } else {
            _tmpRankingSource = _stmt.getText(_cursorIndexOfRankingSource)
          }
          val _tmpModelScore: Double?
          if (_stmt.isNull(_cursorIndexOfModelScore)) {
            _tmpModelScore = null
          } else {
            _tmpModelScore = _stmt.getDouble(_cursorIndexOfModelScore)
          }
          val _tmpAlpha: Double?
          if (_stmt.isNull(_cursorIndexOfAlpha)) {
            _tmpAlpha = null
          } else {
            _tmpAlpha = _stmt.getDouble(_cursorIndexOfAlpha)
          }
          val _tmpAlphaXScore: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXScore)) {
            _tmpAlphaXScore = null
          } else {
            _tmpAlphaXScore = _stmt.getDouble(_cursorIndexOfAlphaXScore)
          }
          val _tmpAlphaXRank: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXRank)) {
            _tmpAlphaXRank = null
          } else {
            _tmpAlphaXRank = _stmt.getDouble(_cursorIndexOfAlphaXRank)
          }
          val _tmpAlphaXConfidence: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXConfidence)) {
            _tmpAlphaXConfidence = null
          } else {
            _tmpAlphaXConfidence = _stmt.getDouble(_cursorIndexOfAlphaXConfidence)
          }
          val _tmpAlphaCoreEligible: Int
          _tmpAlphaCoreEligible = _stmt.getLong(_cursorIndexOfAlphaCoreEligible).toInt()
          val _tmpAlphaXEligible: Int
          _tmpAlphaXEligible = _stmt.getLong(_cursorIndexOfAlphaXEligible).toInt()
          val _tmpAlphaReason: String?
          if (_stmt.isNull(_cursorIndexOfAlphaReason)) {
            _tmpAlphaReason = null
          } else {
            _tmpAlphaReason = _stmt.getText(_cursorIndexOfAlphaReason)
          }
          val _tmpAlphaPrimaryBlocker: String?
          if (_stmt.isNull(_cursorIndexOfAlphaPrimaryBlocker)) {
            _tmpAlphaPrimaryBlocker = null
          } else {
            _tmpAlphaPrimaryBlocker = _stmt.getText(_cursorIndexOfAlphaPrimaryBlocker)
          }
          val _tmpAlphaResearchBucket: String?
          if (_stmt.isNull(_cursorIndexOfAlphaResearchBucket)) {
            _tmpAlphaResearchBucket = null
          } else {
            _tmpAlphaResearchBucket = _stmt.getText(_cursorIndexOfAlphaResearchBucket)
          }
          val _tmpAlphaSnapshotStreak: Int?
          if (_stmt.isNull(_cursorIndexOfAlphaSnapshotStreak)) {
            _tmpAlphaSnapshotStreak = null
          } else {
            _tmpAlphaSnapshotStreak = _stmt.getLong(_cursorIndexOfAlphaSnapshotStreak).toInt()
          }
          val _tmpAiInsight: String?
          if (_stmt.isNull(_cursorIndexOfAiInsight)) {
            _tmpAiInsight = null
          } else {
            _tmpAiInsight = _stmt.getText(_cursorIndexOfAiInsight)
          }
          val _tmpRisk: String?
          if (_stmt.isNull(_cursorIndexOfRisk)) {
            _tmpRisk = null
          } else {
            _tmpRisk = _stmt.getText(_cursorIndexOfRisk)
          }
          val _tmpDataCompleteness: Double?
          if (_stmt.isNull(_cursorIndexOfDataCompleteness)) {
            _tmpDataCompleteness = null
          } else {
            _tmpDataCompleteness = _stmt.getDouble(_cursorIndexOfDataCompleteness)
          }
          val _tmpScoringDate: String?
          if (_stmt.isNull(_cursorIndexOfScoringDate)) {
            _tmpScoringDate = null
          } else {
            _tmpScoringDate = _stmt.getText(_cursorIndexOfScoringDate)
          }
          val _tmpModelUsed: String?
          if (_stmt.isNull(_cursorIndexOfModelUsed)) {
            _tmpModelUsed = null
          } else {
            _tmpModelUsed = _stmt.getText(_cursorIndexOfModelUsed)
          }
          val _tmpBuffett: Double?
          if (_stmt.isNull(_cursorIndexOfBuffett)) {
            _tmpBuffett = null
          } else {
            _tmpBuffett = _stmt.getDouble(_cursorIndexOfBuffett)
          }
          val _tmpGraham: Double?
          if (_stmt.isNull(_cursorIndexOfGraham)) {
            _tmpGraham = null
          } else {
            _tmpGraham = _stmt.getDouble(_cursorIndexOfGraham)
          }
          val _tmpPiotroski: Double?
          if (_stmt.isNull(_cursorIndexOfPiotroski)) {
            _tmpPiotroski = null
          } else {
            _tmpPiotroski = _stmt.getDouble(_cursorIndexOfPiotroski)
          }
          val _tmpPiotroskiRaw: Int?
          if (_stmt.isNull(_cursorIndexOfPiotroskiRaw)) {
            _tmpPiotroskiRaw = null
          } else {
            _tmpPiotroskiRaw = _stmt.getLong(_cursorIndexOfPiotroskiRaw).toInt()
          }
          val _tmpMagicFormula: Double?
          if (_stmt.isNull(_cursorIndexOfMagicFormula)) {
            _tmpMagicFormula = null
          } else {
            _tmpMagicFormula = _stmt.getDouble(_cursorIndexOfMagicFormula)
          }
          val _tmpLynchPeg: Double?
          if (_stmt.isNull(_cursorIndexOfLynchPeg)) {
            _tmpLynchPeg = null
          } else {
            _tmpLynchPeg = _stmt.getDouble(_cursorIndexOfLynchPeg)
          }
          val _tmpDcfMos: Double?
          if (_stmt.isNull(_cursorIndexOfDcfMos)) {
            _tmpDcfMos = null
          } else {
            _tmpDcfMos = _stmt.getDouble(_cursorIndexOfDcfMos)
          }
          val _tmpMomentum: Double?
          if (_stmt.isNull(_cursorIndexOfMomentum)) {
            _tmpMomentum = null
          } else {
            _tmpMomentum = _stmt.getDouble(_cursorIndexOfMomentum)
          }
          val _tmpInsider: Double?
          if (_stmt.isNull(_cursorIndexOfInsider)) {
            _tmpInsider = null
          } else {
            _tmpInsider = _stmt.getDouble(_cursorIndexOfInsider)
          }
          val _tmpTechnical: Double?
          if (_stmt.isNull(_cursorIndexOfTechnical)) {
            _tmpTechnical = null
          } else {
            _tmpTechnical = _stmt.getDouble(_cursorIndexOfTechnical)
          }
          val _tmpDividend: Double?
          if (_stmt.isNull(_cursorIndexOfDividend)) {
            _tmpDividend = null
          } else {
            _tmpDividend = _stmt.getDouble(_cursorIndexOfDividend)
          }
          val _tmpBeta: Double?
          if (_stmt.isNull(_cursorIndexOfBeta)) {
            _tmpBeta = null
          } else {
            _tmpBeta = _stmt.getDouble(_cursorIndexOfBeta)
          }
          val _tmpDelta: Double?
          if (_stmt.isNull(_cursorIndexOfDelta)) {
            _tmpDelta = null
          } else {
            _tmpDelta = _stmt.getDouble(_cursorIndexOfDelta)
          }
          val _tmpQualityFlagsJson: String?
          if (_stmt.isNull(_cursorIndexOfQualityFlagsJson)) {
            _tmpQualityFlagsJson = null
          } else {
            _tmpQualityFlagsJson = _stmt.getText(_cursorIndexOfQualityFlagsJson)
          }
          val _tmpDcfIntrinsicValue: Double?
          if (_stmt.isNull(_cursorIndexOfDcfIntrinsicValue)) {
            _tmpDcfIntrinsicValue = null
          } else {
            _tmpDcfIntrinsicValue = _stmt.getDouble(_cursorIndexOfDcfIntrinsicValue)
          }
          val _tmpDcfGrowthRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfGrowthRatePct)) {
            _tmpDcfGrowthRatePct = null
          } else {
            _tmpDcfGrowthRatePct = _stmt.getDouble(_cursorIndexOfDcfGrowthRatePct)
          }
          val _tmpDcfDiscountRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfDiscountRatePct)) {
            _tmpDcfDiscountRatePct = null
          } else {
            _tmpDcfDiscountRatePct = _stmt.getDouble(_cursorIndexOfDcfDiscountRatePct)
          }
          val _tmpDcfTerminalGrowthPct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfTerminalGrowthPct)) {
            _tmpDcfTerminalGrowthPct = null
          } else {
            _tmpDcfTerminalGrowthPct = _stmt.getDouble(_cursorIndexOfDcfTerminalGrowthPct)
          }
          _item =
              ScoringLatestEntity(_tmpCompanyId,_tmpTicker,_tmpName,_tmpSector,_tmpType,_tmpIsBist100,_tmpIsActive,_tmpFreeFloatPct,_tmpAvgVolumeTry,_tmpRankingScore,_tmpRankingSource,_tmpModelScore,_tmpAlpha,_tmpAlphaXScore,_tmpAlphaXRank,_tmpAlphaXConfidence,_tmpAlphaCoreEligible,_tmpAlphaXEligible,_tmpAlphaReason,_tmpAlphaPrimaryBlocker,_tmpAlphaResearchBucket,_tmpAlphaSnapshotStreak,_tmpAiInsight,_tmpRisk,_tmpDataCompleteness,_tmpScoringDate,_tmpModelUsed,_tmpBuffett,_tmpGraham,_tmpPiotroski,_tmpPiotroskiRaw,_tmpMagicFormula,_tmpLynchPeg,_tmpDcfMos,_tmpMomentum,_tmpInsider,_tmpTechnical,_tmpDividend,_tmpBeta,_tmpDelta,_tmpQualityFlagsJson,_tmpDcfIntrinsicValue,_tmpDcfGrowthRatePct,_tmpDcfDiscountRatePct,_tmpDcfTerminalGrowthPct)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countScoring(
    onlyAlphaCore: Int,
    onlyAlphaX: Int,
    onlyBist100: Int,
    sector: String?,
    risk: String?,
    minScore: Double?,
    search: String?,
  ): Int {
    val _sql: String = """
        |
        |        SELECT count(*) FROM scoring_latest 
        |        WHERE (? = 0 OR alpha_core_eligible = 1)
        |          AND (? = 0 OR alpha_x_eligible = 1)
        |          AND (? = 0 OR is_bist100 = 1)
        |          AND (? IS NULL OR sector = ?)
        |          AND (? IS NULL OR risk = ?)
        |          AND (? IS NULL OR ranking_score >= ?)
        |          AND (? IS NULL OR ticker LIKE '%' || ? || '%' OR name LIKE '%' || ? || '%')
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, onlyAlphaCore.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, onlyAlphaX.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, onlyBist100.toLong())
        _argIndex = 4
        if (sector == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, sector)
        }
        _argIndex = 5
        if (sector == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, sector)
        }
        _argIndex = 6
        if (risk == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, risk)
        }
        _argIndex = 7
        if (risk == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, risk)
        }
        _argIndex = 8
        if (minScore == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindDouble(_argIndex, minScore)
        }
        _argIndex = 9
        if (minScore == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindDouble(_argIndex, minScore)
        }
        _argIndex = 10
        if (search == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, search)
        }
        _argIndex = 11
        if (search == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, search)
        }
        _argIndex = 12
        if (search == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, search)
        }
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun listSectors(): List<String> {
    val _sql: String =
        "SELECT DISTINCT sector FROM scoring_latest WHERE sector IS NOT NULL ORDER BY sector"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getScoring(ticker: String): ScoringLatestEntity? {
    val _sql: String = "SELECT * FROM scoring_latest WHERE ticker = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ticker)
        val _cursorIndexOfCompanyId: Int = getColumnIndexOrThrow(_stmt, "company_id")
        val _cursorIndexOfTicker: Int = getColumnIndexOrThrow(_stmt, "ticker")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfSector: Int = getColumnIndexOrThrow(_stmt, "sector")
        val _cursorIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _cursorIndexOfIsBist100: Int = getColumnIndexOrThrow(_stmt, "is_bist100")
        val _cursorIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "is_active")
        val _cursorIndexOfFreeFloatPct: Int = getColumnIndexOrThrow(_stmt, "free_float_pct")
        val _cursorIndexOfAvgVolumeTry: Int = getColumnIndexOrThrow(_stmt, "avg_volume_try")
        val _cursorIndexOfRankingScore: Int = getColumnIndexOrThrow(_stmt, "ranking_score")
        val _cursorIndexOfRankingSource: Int = getColumnIndexOrThrow(_stmt, "ranking_source")
        val _cursorIndexOfModelScore: Int = getColumnIndexOrThrow(_stmt, "model_score")
        val _cursorIndexOfAlpha: Int = getColumnIndexOrThrow(_stmt, "alpha")
        val _cursorIndexOfAlphaXScore: Int = getColumnIndexOrThrow(_stmt, "alpha_x_score")
        val _cursorIndexOfAlphaXRank: Int = getColumnIndexOrThrow(_stmt, "alpha_x_rank")
        val _cursorIndexOfAlphaXConfidence: Int = getColumnIndexOrThrow(_stmt, "alpha_x_confidence")
        val _cursorIndexOfAlphaCoreEligible: Int = getColumnIndexOrThrow(_stmt,
            "alpha_core_eligible")
        val _cursorIndexOfAlphaXEligible: Int = getColumnIndexOrThrow(_stmt, "alpha_x_eligible")
        val _cursorIndexOfAlphaReason: Int = getColumnIndexOrThrow(_stmt, "alpha_reason")
        val _cursorIndexOfAlphaPrimaryBlocker: Int = getColumnIndexOrThrow(_stmt,
            "alpha_primary_blocker")
        val _cursorIndexOfAlphaResearchBucket: Int = getColumnIndexOrThrow(_stmt,
            "alpha_research_bucket")
        val _cursorIndexOfAlphaSnapshotStreak: Int = getColumnIndexOrThrow(_stmt,
            "alpha_snapshot_streak")
        val _cursorIndexOfAiInsight: Int = getColumnIndexOrThrow(_stmt, "ai_insight")
        val _cursorIndexOfRisk: Int = getColumnIndexOrThrow(_stmt, "risk")
        val _cursorIndexOfDataCompleteness: Int = getColumnIndexOrThrow(_stmt, "data_completeness")
        val _cursorIndexOfScoringDate: Int = getColumnIndexOrThrow(_stmt, "scoring_date")
        val _cursorIndexOfModelUsed: Int = getColumnIndexOrThrow(_stmt, "model_used")
        val _cursorIndexOfBuffett: Int = getColumnIndexOrThrow(_stmt, "buffett")
        val _cursorIndexOfGraham: Int = getColumnIndexOrThrow(_stmt, "graham")
        val _cursorIndexOfPiotroski: Int = getColumnIndexOrThrow(_stmt, "piotroski")
        val _cursorIndexOfPiotroskiRaw: Int = getColumnIndexOrThrow(_stmt, "piotroski_raw")
        val _cursorIndexOfMagicFormula: Int = getColumnIndexOrThrow(_stmt, "magic_formula")
        val _cursorIndexOfLynchPeg: Int = getColumnIndexOrThrow(_stmt, "lynch_peg")
        val _cursorIndexOfDcfMos: Int = getColumnIndexOrThrow(_stmt, "dcf_mos")
        val _cursorIndexOfMomentum: Int = getColumnIndexOrThrow(_stmt, "momentum")
        val _cursorIndexOfInsider: Int = getColumnIndexOrThrow(_stmt, "insider")
        val _cursorIndexOfTechnical: Int = getColumnIndexOrThrow(_stmt, "technical")
        val _cursorIndexOfDividend: Int = getColumnIndexOrThrow(_stmt, "dividend")
        val _cursorIndexOfBeta: Int = getColumnIndexOrThrow(_stmt, "beta")
        val _cursorIndexOfDelta: Int = getColumnIndexOrThrow(_stmt, "delta")
        val _cursorIndexOfQualityFlagsJson: Int = getColumnIndexOrThrow(_stmt, "quality_flags_json")
        val _cursorIndexOfDcfIntrinsicValue: Int = getColumnIndexOrThrow(_stmt,
            "dcf_intrinsic_value")
        val _cursorIndexOfDcfGrowthRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_growth_rate_pct")
        val _cursorIndexOfDcfDiscountRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_discount_rate_pct")
        val _cursorIndexOfDcfTerminalGrowthPct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_terminal_growth_pct")
        val _result: ScoringLatestEntity?
        if (_stmt.step()) {
          val _tmpCompanyId: Int
          _tmpCompanyId = _stmt.getLong(_cursorIndexOfCompanyId).toInt()
          val _tmpTicker: String
          _tmpTicker = _stmt.getText(_cursorIndexOfTicker)
          val _tmpName: String?
          if (_stmt.isNull(_cursorIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_cursorIndexOfName)
          }
          val _tmpSector: String?
          if (_stmt.isNull(_cursorIndexOfSector)) {
            _tmpSector = null
          } else {
            _tmpSector = _stmt.getText(_cursorIndexOfSector)
          }
          val _tmpType: String?
          if (_stmt.isNull(_cursorIndexOfType)) {
            _tmpType = null
          } else {
            _tmpType = _stmt.getText(_cursorIndexOfType)
          }
          val _tmpIsBist100: Int
          _tmpIsBist100 = _stmt.getLong(_cursorIndexOfIsBist100).toInt()
          val _tmpIsActive: Int
          _tmpIsActive = _stmt.getLong(_cursorIndexOfIsActive).toInt()
          val _tmpFreeFloatPct: Double?
          if (_stmt.isNull(_cursorIndexOfFreeFloatPct)) {
            _tmpFreeFloatPct = null
          } else {
            _tmpFreeFloatPct = _stmt.getDouble(_cursorIndexOfFreeFloatPct)
          }
          val _tmpAvgVolumeTry: Double?
          if (_stmt.isNull(_cursorIndexOfAvgVolumeTry)) {
            _tmpAvgVolumeTry = null
          } else {
            _tmpAvgVolumeTry = _stmt.getDouble(_cursorIndexOfAvgVolumeTry)
          }
          val _tmpRankingScore: Double?
          if (_stmt.isNull(_cursorIndexOfRankingScore)) {
            _tmpRankingScore = null
          } else {
            _tmpRankingScore = _stmt.getDouble(_cursorIndexOfRankingScore)
          }
          val _tmpRankingSource: String?
          if (_stmt.isNull(_cursorIndexOfRankingSource)) {
            _tmpRankingSource = null
          } else {
            _tmpRankingSource = _stmt.getText(_cursorIndexOfRankingSource)
          }
          val _tmpModelScore: Double?
          if (_stmt.isNull(_cursorIndexOfModelScore)) {
            _tmpModelScore = null
          } else {
            _tmpModelScore = _stmt.getDouble(_cursorIndexOfModelScore)
          }
          val _tmpAlpha: Double?
          if (_stmt.isNull(_cursorIndexOfAlpha)) {
            _tmpAlpha = null
          } else {
            _tmpAlpha = _stmt.getDouble(_cursorIndexOfAlpha)
          }
          val _tmpAlphaXScore: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXScore)) {
            _tmpAlphaXScore = null
          } else {
            _tmpAlphaXScore = _stmt.getDouble(_cursorIndexOfAlphaXScore)
          }
          val _tmpAlphaXRank: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXRank)) {
            _tmpAlphaXRank = null
          } else {
            _tmpAlphaXRank = _stmt.getDouble(_cursorIndexOfAlphaXRank)
          }
          val _tmpAlphaXConfidence: Double?
          if (_stmt.isNull(_cursorIndexOfAlphaXConfidence)) {
            _tmpAlphaXConfidence = null
          } else {
            _tmpAlphaXConfidence = _stmt.getDouble(_cursorIndexOfAlphaXConfidence)
          }
          val _tmpAlphaCoreEligible: Int
          _tmpAlphaCoreEligible = _stmt.getLong(_cursorIndexOfAlphaCoreEligible).toInt()
          val _tmpAlphaXEligible: Int
          _tmpAlphaXEligible = _stmt.getLong(_cursorIndexOfAlphaXEligible).toInt()
          val _tmpAlphaReason: String?
          if (_stmt.isNull(_cursorIndexOfAlphaReason)) {
            _tmpAlphaReason = null
          } else {
            _tmpAlphaReason = _stmt.getText(_cursorIndexOfAlphaReason)
          }
          val _tmpAlphaPrimaryBlocker: String?
          if (_stmt.isNull(_cursorIndexOfAlphaPrimaryBlocker)) {
            _tmpAlphaPrimaryBlocker = null
          } else {
            _tmpAlphaPrimaryBlocker = _stmt.getText(_cursorIndexOfAlphaPrimaryBlocker)
          }
          val _tmpAlphaResearchBucket: String?
          if (_stmt.isNull(_cursorIndexOfAlphaResearchBucket)) {
            _tmpAlphaResearchBucket = null
          } else {
            _tmpAlphaResearchBucket = _stmt.getText(_cursorIndexOfAlphaResearchBucket)
          }
          val _tmpAlphaSnapshotStreak: Int?
          if (_stmt.isNull(_cursorIndexOfAlphaSnapshotStreak)) {
            _tmpAlphaSnapshotStreak = null
          } else {
            _tmpAlphaSnapshotStreak = _stmt.getLong(_cursorIndexOfAlphaSnapshotStreak).toInt()
          }
          val _tmpAiInsight: String?
          if (_stmt.isNull(_cursorIndexOfAiInsight)) {
            _tmpAiInsight = null
          } else {
            _tmpAiInsight = _stmt.getText(_cursorIndexOfAiInsight)
          }
          val _tmpRisk: String?
          if (_stmt.isNull(_cursorIndexOfRisk)) {
            _tmpRisk = null
          } else {
            _tmpRisk = _stmt.getText(_cursorIndexOfRisk)
          }
          val _tmpDataCompleteness: Double?
          if (_stmt.isNull(_cursorIndexOfDataCompleteness)) {
            _tmpDataCompleteness = null
          } else {
            _tmpDataCompleteness = _stmt.getDouble(_cursorIndexOfDataCompleteness)
          }
          val _tmpScoringDate: String?
          if (_stmt.isNull(_cursorIndexOfScoringDate)) {
            _tmpScoringDate = null
          } else {
            _tmpScoringDate = _stmt.getText(_cursorIndexOfScoringDate)
          }
          val _tmpModelUsed: String?
          if (_stmt.isNull(_cursorIndexOfModelUsed)) {
            _tmpModelUsed = null
          } else {
            _tmpModelUsed = _stmt.getText(_cursorIndexOfModelUsed)
          }
          val _tmpBuffett: Double?
          if (_stmt.isNull(_cursorIndexOfBuffett)) {
            _tmpBuffett = null
          } else {
            _tmpBuffett = _stmt.getDouble(_cursorIndexOfBuffett)
          }
          val _tmpGraham: Double?
          if (_stmt.isNull(_cursorIndexOfGraham)) {
            _tmpGraham = null
          } else {
            _tmpGraham = _stmt.getDouble(_cursorIndexOfGraham)
          }
          val _tmpPiotroski: Double?
          if (_stmt.isNull(_cursorIndexOfPiotroski)) {
            _tmpPiotroski = null
          } else {
            _tmpPiotroski = _stmt.getDouble(_cursorIndexOfPiotroski)
          }
          val _tmpPiotroskiRaw: Int?
          if (_stmt.isNull(_cursorIndexOfPiotroskiRaw)) {
            _tmpPiotroskiRaw = null
          } else {
            _tmpPiotroskiRaw = _stmt.getLong(_cursorIndexOfPiotroskiRaw).toInt()
          }
          val _tmpMagicFormula: Double?
          if (_stmt.isNull(_cursorIndexOfMagicFormula)) {
            _tmpMagicFormula = null
          } else {
            _tmpMagicFormula = _stmt.getDouble(_cursorIndexOfMagicFormula)
          }
          val _tmpLynchPeg: Double?
          if (_stmt.isNull(_cursorIndexOfLynchPeg)) {
            _tmpLynchPeg = null
          } else {
            _tmpLynchPeg = _stmt.getDouble(_cursorIndexOfLynchPeg)
          }
          val _tmpDcfMos: Double?
          if (_stmt.isNull(_cursorIndexOfDcfMos)) {
            _tmpDcfMos = null
          } else {
            _tmpDcfMos = _stmt.getDouble(_cursorIndexOfDcfMos)
          }
          val _tmpMomentum: Double?
          if (_stmt.isNull(_cursorIndexOfMomentum)) {
            _tmpMomentum = null
          } else {
            _tmpMomentum = _stmt.getDouble(_cursorIndexOfMomentum)
          }
          val _tmpInsider: Double?
          if (_stmt.isNull(_cursorIndexOfInsider)) {
            _tmpInsider = null
          } else {
            _tmpInsider = _stmt.getDouble(_cursorIndexOfInsider)
          }
          val _tmpTechnical: Double?
          if (_stmt.isNull(_cursorIndexOfTechnical)) {
            _tmpTechnical = null
          } else {
            _tmpTechnical = _stmt.getDouble(_cursorIndexOfTechnical)
          }
          val _tmpDividend: Double?
          if (_stmt.isNull(_cursorIndexOfDividend)) {
            _tmpDividend = null
          } else {
            _tmpDividend = _stmt.getDouble(_cursorIndexOfDividend)
          }
          val _tmpBeta: Double?
          if (_stmt.isNull(_cursorIndexOfBeta)) {
            _tmpBeta = null
          } else {
            _tmpBeta = _stmt.getDouble(_cursorIndexOfBeta)
          }
          val _tmpDelta: Double?
          if (_stmt.isNull(_cursorIndexOfDelta)) {
            _tmpDelta = null
          } else {
            _tmpDelta = _stmt.getDouble(_cursorIndexOfDelta)
          }
          val _tmpQualityFlagsJson: String?
          if (_stmt.isNull(_cursorIndexOfQualityFlagsJson)) {
            _tmpQualityFlagsJson = null
          } else {
            _tmpQualityFlagsJson = _stmt.getText(_cursorIndexOfQualityFlagsJson)
          }
          val _tmpDcfIntrinsicValue: Double?
          if (_stmt.isNull(_cursorIndexOfDcfIntrinsicValue)) {
            _tmpDcfIntrinsicValue = null
          } else {
            _tmpDcfIntrinsicValue = _stmt.getDouble(_cursorIndexOfDcfIntrinsicValue)
          }
          val _tmpDcfGrowthRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfGrowthRatePct)) {
            _tmpDcfGrowthRatePct = null
          } else {
            _tmpDcfGrowthRatePct = _stmt.getDouble(_cursorIndexOfDcfGrowthRatePct)
          }
          val _tmpDcfDiscountRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfDiscountRatePct)) {
            _tmpDcfDiscountRatePct = null
          } else {
            _tmpDcfDiscountRatePct = _stmt.getDouble(_cursorIndexOfDcfDiscountRatePct)
          }
          val _tmpDcfTerminalGrowthPct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfTerminalGrowthPct)) {
            _tmpDcfTerminalGrowthPct = null
          } else {
            _tmpDcfTerminalGrowthPct = _stmt.getDouble(_cursorIndexOfDcfTerminalGrowthPct)
          }
          _result =
              ScoringLatestEntity(_tmpCompanyId,_tmpTicker,_tmpName,_tmpSector,_tmpType,_tmpIsBist100,_tmpIsActive,_tmpFreeFloatPct,_tmpAvgVolumeTry,_tmpRankingScore,_tmpRankingSource,_tmpModelScore,_tmpAlpha,_tmpAlphaXScore,_tmpAlphaXRank,_tmpAlphaXConfidence,_tmpAlphaCoreEligible,_tmpAlphaXEligible,_tmpAlphaReason,_tmpAlphaPrimaryBlocker,_tmpAlphaResearchBucket,_tmpAlphaSnapshotStreak,_tmpAiInsight,_tmpRisk,_tmpDataCompleteness,_tmpScoringDate,_tmpModelUsed,_tmpBuffett,_tmpGraham,_tmpPiotroski,_tmpPiotroskiRaw,_tmpMagicFormula,_tmpLynchPeg,_tmpDcfMos,_tmpMomentum,_tmpInsider,_tmpTechnical,_tmpDividend,_tmpBeta,_tmpDelta,_tmpQualityFlagsJson,_tmpDcfIntrinsicValue,_tmpDcfGrowthRatePct,_tmpDcfDiscountRatePct,_tmpDcfTerminalGrowthPct)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCompany(ticker: String): CompanyEntity? {
    val _sql: String = "SELECT * FROM companies WHERE ticker = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ticker)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTicker: Int = getColumnIndexOrThrow(_stmt, "ticker")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfCompanyType: Int = getColumnIndexOrThrow(_stmt, "company_type")
        val _cursorIndexOfSectorBist: Int = getColumnIndexOrThrow(_stmt, "sector_bist")
        val _cursorIndexOfSectorCustom: Int = getColumnIndexOrThrow(_stmt, "sector_custom")
        val _cursorIndexOfIsBist100: Int = getColumnIndexOrThrow(_stmt, "is_bist100")
        val _cursorIndexOfIsIpo: Int = getColumnIndexOrThrow(_stmt, "is_ipo")
        val _cursorIndexOfFreeFloatPct: Int = getColumnIndexOrThrow(_stmt, "free_float_pct")
        val _cursorIndexOfListingDate: Int = getColumnIndexOrThrow(_stmt, "listing_date")
        val _cursorIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "is_active")
        val _result: CompanyEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpTicker: String
          _tmpTicker = _stmt.getText(_cursorIndexOfTicker)
          val _tmpName: String?
          if (_stmt.isNull(_cursorIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_cursorIndexOfName)
          }
          val _tmpCompanyType: String?
          if (_stmt.isNull(_cursorIndexOfCompanyType)) {
            _tmpCompanyType = null
          } else {
            _tmpCompanyType = _stmt.getText(_cursorIndexOfCompanyType)
          }
          val _tmpSectorBist: String?
          if (_stmt.isNull(_cursorIndexOfSectorBist)) {
            _tmpSectorBist = null
          } else {
            _tmpSectorBist = _stmt.getText(_cursorIndexOfSectorBist)
          }
          val _tmpSectorCustom: String?
          if (_stmt.isNull(_cursorIndexOfSectorCustom)) {
            _tmpSectorCustom = null
          } else {
            _tmpSectorCustom = _stmt.getText(_cursorIndexOfSectorCustom)
          }
          val _tmpIsBist100: Int
          _tmpIsBist100 = _stmt.getLong(_cursorIndexOfIsBist100).toInt()
          val _tmpIsIpo: Int
          _tmpIsIpo = _stmt.getLong(_cursorIndexOfIsIpo).toInt()
          val _tmpFreeFloatPct: Double?
          if (_stmt.isNull(_cursorIndexOfFreeFloatPct)) {
            _tmpFreeFloatPct = null
          } else {
            _tmpFreeFloatPct = _stmt.getDouble(_cursorIndexOfFreeFloatPct)
          }
          val _tmpListingDate: String?
          if (_stmt.isNull(_cursorIndexOfListingDate)) {
            _tmpListingDate = null
          } else {
            _tmpListingDate = _stmt.getText(_cursorIndexOfListingDate)
          }
          val _tmpIsActive: Int
          _tmpIsActive = _stmt.getLong(_cursorIndexOfIsActive).toInt()
          _result =
              CompanyEntity(_tmpId,_tmpTicker,_tmpName,_tmpCompanyType,_tmpSectorBist,_tmpSectorCustom,_tmpIsBist100,_tmpIsIpo,_tmpFreeFloatPct,_tmpListingDate,_tmpIsActive)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getOpenPosition(ticker: String): OpenPositionEntity? {
    val _sql: String = "SELECT * FROM open_positions WHERE ticker = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ticker)
        val _cursorIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sort_order")
        val _cursorIndexOfPortfolio: Int = getColumnIndexOrThrow(_stmt, "portfolio")
        val _cursorIndexOfTicker: Int = getColumnIndexOrThrow(_stmt, "ticker")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfCompanyId: Int = getColumnIndexOrThrow(_stmt, "company_id")
        val _cursorIndexOfEntryPrice: Int = getColumnIndexOrThrow(_stmt, "entry_price")
        val _cursorIndexOfCurrentPrice: Int = getColumnIndexOrThrow(_stmt, "current_price")
        val _cursorIndexOfPnlPct: Int = getColumnIndexOrThrow(_stmt, "pnl_pct")
        val _cursorIndexOfTargetPrice: Int = getColumnIndexOrThrow(_stmt, "target_price")
        val _cursorIndexOfStopLossPrice: Int = getColumnIndexOrThrow(_stmt, "stop_loss_price")
        val _cursorIndexOfStopPctFromEntry: Int = getColumnIndexOrThrow(_stmt,
            "stop_pct_from_entry")
        val _cursorIndexOfCompositeScore: Int = getColumnIndexOrThrow(_stmt, "composite_score")
        val _cursorIndexOfSelectionDate: Int = getColumnIndexOrThrow(_stmt, "selection_date")
        val _cursorIndexOfDaysHeld: Int = getColumnIndexOrThrow(_stmt, "days_held")
        val _cursorIndexOfReasonTopFactorsJson: Int = getColumnIndexOrThrow(_stmt,
            "reason_top_factors_json")
        val _cursorIndexOfQualityFlagsJson: Int = getColumnIndexOrThrow(_stmt, "quality_flags_json")
        val _cursorIndexOfDcfMosPct: Int = getColumnIndexOrThrow(_stmt, "dcf_margin_of_safety_pct")
        val _cursorIndexOfDcfIntrinsicValue: Int = getColumnIndexOrThrow(_stmt,
            "dcf_intrinsic_value")
        val _cursorIndexOfDcfGrowthRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_growth_rate_pct")
        val _cursorIndexOfDcfDiscountRatePct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_discount_rate_pct")
        val _cursorIndexOfDcfTerminalGrowthPct: Int = getColumnIndexOrThrow(_stmt,
            "dcf_terminal_growth_pct")
        val _result: OpenPositionEntity?
        if (_stmt.step()) {
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_cursorIndexOfSortOrder).toInt()
          val _tmpPortfolio: String?
          if (_stmt.isNull(_cursorIndexOfPortfolio)) {
            _tmpPortfolio = null
          } else {
            _tmpPortfolio = _stmt.getText(_cursorIndexOfPortfolio)
          }
          val _tmpTicker: String
          _tmpTicker = _stmt.getText(_cursorIndexOfTicker)
          val _tmpName: String?
          if (_stmt.isNull(_cursorIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_cursorIndexOfName)
          }
          val _tmpCompanyId: Int?
          if (_stmt.isNull(_cursorIndexOfCompanyId)) {
            _tmpCompanyId = null
          } else {
            _tmpCompanyId = _stmt.getLong(_cursorIndexOfCompanyId).toInt()
          }
          val _tmpEntryPrice: Double?
          if (_stmt.isNull(_cursorIndexOfEntryPrice)) {
            _tmpEntryPrice = null
          } else {
            _tmpEntryPrice = _stmt.getDouble(_cursorIndexOfEntryPrice)
          }
          val _tmpCurrentPrice: Double?
          if (_stmt.isNull(_cursorIndexOfCurrentPrice)) {
            _tmpCurrentPrice = null
          } else {
            _tmpCurrentPrice = _stmt.getDouble(_cursorIndexOfCurrentPrice)
          }
          val _tmpPnlPct: Double?
          if (_stmt.isNull(_cursorIndexOfPnlPct)) {
            _tmpPnlPct = null
          } else {
            _tmpPnlPct = _stmt.getDouble(_cursorIndexOfPnlPct)
          }
          val _tmpTargetPrice: Double?
          if (_stmt.isNull(_cursorIndexOfTargetPrice)) {
            _tmpTargetPrice = null
          } else {
            _tmpTargetPrice = _stmt.getDouble(_cursorIndexOfTargetPrice)
          }
          val _tmpStopLossPrice: Double?
          if (_stmt.isNull(_cursorIndexOfStopLossPrice)) {
            _tmpStopLossPrice = null
          } else {
            _tmpStopLossPrice = _stmt.getDouble(_cursorIndexOfStopLossPrice)
          }
          val _tmpStopPctFromEntry: Double?
          if (_stmt.isNull(_cursorIndexOfStopPctFromEntry)) {
            _tmpStopPctFromEntry = null
          } else {
            _tmpStopPctFromEntry = _stmt.getDouble(_cursorIndexOfStopPctFromEntry)
          }
          val _tmpCompositeScore: Double?
          if (_stmt.isNull(_cursorIndexOfCompositeScore)) {
            _tmpCompositeScore = null
          } else {
            _tmpCompositeScore = _stmt.getDouble(_cursorIndexOfCompositeScore)
          }
          val _tmpSelectionDate: String?
          if (_stmt.isNull(_cursorIndexOfSelectionDate)) {
            _tmpSelectionDate = null
          } else {
            _tmpSelectionDate = _stmt.getText(_cursorIndexOfSelectionDate)
          }
          val _tmpDaysHeld: Int?
          if (_stmt.isNull(_cursorIndexOfDaysHeld)) {
            _tmpDaysHeld = null
          } else {
            _tmpDaysHeld = _stmt.getLong(_cursorIndexOfDaysHeld).toInt()
          }
          val _tmpReasonTopFactorsJson: String?
          if (_stmt.isNull(_cursorIndexOfReasonTopFactorsJson)) {
            _tmpReasonTopFactorsJson = null
          } else {
            _tmpReasonTopFactorsJson = _stmt.getText(_cursorIndexOfReasonTopFactorsJson)
          }
          val _tmpQualityFlagsJson: String?
          if (_stmt.isNull(_cursorIndexOfQualityFlagsJson)) {
            _tmpQualityFlagsJson = null
          } else {
            _tmpQualityFlagsJson = _stmt.getText(_cursorIndexOfQualityFlagsJson)
          }
          val _tmpDcfMosPct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfMosPct)) {
            _tmpDcfMosPct = null
          } else {
            _tmpDcfMosPct = _stmt.getDouble(_cursorIndexOfDcfMosPct)
          }
          val _tmpDcfIntrinsicValue: Double?
          if (_stmt.isNull(_cursorIndexOfDcfIntrinsicValue)) {
            _tmpDcfIntrinsicValue = null
          } else {
            _tmpDcfIntrinsicValue = _stmt.getDouble(_cursorIndexOfDcfIntrinsicValue)
          }
          val _tmpDcfGrowthRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfGrowthRatePct)) {
            _tmpDcfGrowthRatePct = null
          } else {
            _tmpDcfGrowthRatePct = _stmt.getDouble(_cursorIndexOfDcfGrowthRatePct)
          }
          val _tmpDcfDiscountRatePct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfDiscountRatePct)) {
            _tmpDcfDiscountRatePct = null
          } else {
            _tmpDcfDiscountRatePct = _stmt.getDouble(_cursorIndexOfDcfDiscountRatePct)
          }
          val _tmpDcfTerminalGrowthPct: Double?
          if (_stmt.isNull(_cursorIndexOfDcfTerminalGrowthPct)) {
            _tmpDcfTerminalGrowthPct = null
          } else {
            _tmpDcfTerminalGrowthPct = _stmt.getDouble(_cursorIndexOfDcfTerminalGrowthPct)
          }
          _result =
              OpenPositionEntity(_tmpSortOrder,_tmpPortfolio,_tmpTicker,_tmpName,_tmpCompanyId,_tmpEntryPrice,_tmpCurrentPrice,_tmpPnlPct,_tmpTargetPrice,_tmpStopLossPrice,_tmpStopPctFromEntry,_tmpCompositeScore,_tmpSelectionDate,_tmpDaysHeld,_tmpReasonTopFactorsJson,_tmpQualityFlagsJson,_tmpDcfMosPct,_tmpDcfIntrinsicValue,_tmpDcfGrowthRatePct,_tmpDcfDiscountRatePct,_tmpDcfTerminalGrowthPct)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAdjustedMetrics(companyId: Int): AdjustedMetricsEntity? {
    val _sql: String = "SELECT * FROM adjusted_metrics_latest WHERE company_id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, companyId.toLong())
        val _cursorIndexOfCompanyId: Int = getColumnIndexOrThrow(_stmt, "company_id")
        val _cursorIndexOfPeriodEnd: Int = getColumnIndexOrThrow(_stmt, "period_end")
        val _cursorIndexOfReportedNetIncome: Int = getColumnIndexOrThrow(_stmt,
            "reported_net_income")
        val _cursorIndexOfMonetaryGainLoss: Int = getColumnIndexOrThrow(_stmt, "monetary_gain_loss")
        val _cursorIndexOfAdjustedNetIncome: Int = getColumnIndexOrThrow(_stmt,
            "adjusted_net_income")
        val _cursorIndexOfOwnerEarnings: Int = getColumnIndexOrThrow(_stmt, "owner_earnings")
        val _cursorIndexOfFreeCashFlow: Int = getColumnIndexOrThrow(_stmt, "free_cash_flow")
        val _cursorIndexOfRoeAdjusted: Int = getColumnIndexOrThrow(_stmt, "roe_adjusted")
        val _cursorIndexOfRoaAdjusted: Int = getColumnIndexOrThrow(_stmt, "roa_adjusted")
        val _cursorIndexOfEpsAdjusted: Int = getColumnIndexOrThrow(_stmt, "eps_adjusted")
        val _cursorIndexOfRealEpsGrowthPct: Int = getColumnIndexOrThrow(_stmt,
            "real_eps_growth_pct")
        val _cursorIndexOfRelatedPartyRevenuePct: Int = getColumnIndexOrThrow(_stmt,
            "related_party_revenue_pct")
        val _cursorIndexOfMaintenanceCapex: Int = getColumnIndexOrThrow(_stmt, "maintenance_capex")
        val _cursorIndexOfGrowthCapex: Int = getColumnIndexOrThrow(_stmt, "growth_capex")
        val _result: AdjustedMetricsEntity?
        if (_stmt.step()) {
          val _tmpCompanyId: Int
          _tmpCompanyId = _stmt.getLong(_cursorIndexOfCompanyId).toInt()
          val _tmpPeriodEnd: String?
          if (_stmt.isNull(_cursorIndexOfPeriodEnd)) {
            _tmpPeriodEnd = null
          } else {
            _tmpPeriodEnd = _stmt.getText(_cursorIndexOfPeriodEnd)
          }
          val _tmpReportedNetIncome: Double?
          if (_stmt.isNull(_cursorIndexOfReportedNetIncome)) {
            _tmpReportedNetIncome = null
          } else {
            _tmpReportedNetIncome = _stmt.getDouble(_cursorIndexOfReportedNetIncome)
          }
          val _tmpMonetaryGainLoss: Double?
          if (_stmt.isNull(_cursorIndexOfMonetaryGainLoss)) {
            _tmpMonetaryGainLoss = null
          } else {
            _tmpMonetaryGainLoss = _stmt.getDouble(_cursorIndexOfMonetaryGainLoss)
          }
          val _tmpAdjustedNetIncome: Double?
          if (_stmt.isNull(_cursorIndexOfAdjustedNetIncome)) {
            _tmpAdjustedNetIncome = null
          } else {
            _tmpAdjustedNetIncome = _stmt.getDouble(_cursorIndexOfAdjustedNetIncome)
          }
          val _tmpOwnerEarnings: Double?
          if (_stmt.isNull(_cursorIndexOfOwnerEarnings)) {
            _tmpOwnerEarnings = null
          } else {
            _tmpOwnerEarnings = _stmt.getDouble(_cursorIndexOfOwnerEarnings)
          }
          val _tmpFreeCashFlow: Double?
          if (_stmt.isNull(_cursorIndexOfFreeCashFlow)) {
            _tmpFreeCashFlow = null
          } else {
            _tmpFreeCashFlow = _stmt.getDouble(_cursorIndexOfFreeCashFlow)
          }
          val _tmpRoeAdjusted: Double?
          if (_stmt.isNull(_cursorIndexOfRoeAdjusted)) {
            _tmpRoeAdjusted = null
          } else {
            _tmpRoeAdjusted = _stmt.getDouble(_cursorIndexOfRoeAdjusted)
          }
          val _tmpRoaAdjusted: Double?
          if (_stmt.isNull(_cursorIndexOfRoaAdjusted)) {
            _tmpRoaAdjusted = null
          } else {
            _tmpRoaAdjusted = _stmt.getDouble(_cursorIndexOfRoaAdjusted)
          }
          val _tmpEpsAdjusted: Double?
          if (_stmt.isNull(_cursorIndexOfEpsAdjusted)) {
            _tmpEpsAdjusted = null
          } else {
            _tmpEpsAdjusted = _stmt.getDouble(_cursorIndexOfEpsAdjusted)
          }
          val _tmpRealEpsGrowthPct: Double?
          if (_stmt.isNull(_cursorIndexOfRealEpsGrowthPct)) {
            _tmpRealEpsGrowthPct = null
          } else {
            _tmpRealEpsGrowthPct = _stmt.getDouble(_cursorIndexOfRealEpsGrowthPct)
          }
          val _tmpRelatedPartyRevenuePct: Double?
          if (_stmt.isNull(_cursorIndexOfRelatedPartyRevenuePct)) {
            _tmpRelatedPartyRevenuePct = null
          } else {
            _tmpRelatedPartyRevenuePct = _stmt.getDouble(_cursorIndexOfRelatedPartyRevenuePct)
          }
          val _tmpMaintenanceCapex: Double?
          if (_stmt.isNull(_cursorIndexOfMaintenanceCapex)) {
            _tmpMaintenanceCapex = null
          } else {
            _tmpMaintenanceCapex = _stmt.getDouble(_cursorIndexOfMaintenanceCapex)
          }
          val _tmpGrowthCapex: Double?
          if (_stmt.isNull(_cursorIndexOfGrowthCapex)) {
            _tmpGrowthCapex = null
          } else {
            _tmpGrowthCapex = _stmt.getDouble(_cursorIndexOfGrowthCapex)
          }
          _result =
              AdjustedMetricsEntity(_tmpCompanyId,_tmpPeriodEnd,_tmpReportedNetIncome,_tmpMonetaryGainLoss,_tmpAdjustedNetIncome,_tmpOwnerEarnings,_tmpFreeCashFlow,_tmpRoeAdjusted,_tmpRoaAdjusted,_tmpEpsAdjusted,_tmpRealEpsGrowthPct,_tmpRelatedPartyRevenuePct,_tmpMaintenanceCapex,_tmpGrowthCapex)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPriceHistory(companyId: Int): List<PriceHistoryEntity> {
    val _sql: String = "SELECT * FROM price_history_730d WHERE company_id = ? ORDER BY date"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, companyId.toLong())
        val _cursorIndexOfCompanyId: Int = getColumnIndexOrThrow(_stmt, "company_id")
        val _cursorIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _cursorIndexOfOpen: Int = getColumnIndexOrThrow(_stmt, "open")
        val _cursorIndexOfHigh: Int = getColumnIndexOrThrow(_stmt, "high")
        val _cursorIndexOfLow: Int = getColumnIndexOrThrow(_stmt, "low")
        val _cursorIndexOfClose: Int = getColumnIndexOrThrow(_stmt, "close")
        val _cursorIndexOfVolume: Int = getColumnIndexOrThrow(_stmt, "volume")
        val _cursorIndexOfAdjustedClose: Int = getColumnIndexOrThrow(_stmt, "adjusted_close")
        val _result: MutableList<PriceHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PriceHistoryEntity
          val _tmpCompanyId: Int
          _tmpCompanyId = _stmt.getLong(_cursorIndexOfCompanyId).toInt()
          val _tmpDate: String
          _tmpDate = _stmt.getText(_cursorIndexOfDate)
          val _tmpOpen: Double?
          if (_stmt.isNull(_cursorIndexOfOpen)) {
            _tmpOpen = null
          } else {
            _tmpOpen = _stmt.getDouble(_cursorIndexOfOpen)
          }
          val _tmpHigh: Double?
          if (_stmt.isNull(_cursorIndexOfHigh)) {
            _tmpHigh = null
          } else {
            _tmpHigh = _stmt.getDouble(_cursorIndexOfHigh)
          }
          val _tmpLow: Double?
          if (_stmt.isNull(_cursorIndexOfLow)) {
            _tmpLow = null
          } else {
            _tmpLow = _stmt.getDouble(_cursorIndexOfLow)
          }
          val _tmpClose: Double?
          if (_stmt.isNull(_cursorIndexOfClose)) {
            _tmpClose = null
          } else {
            _tmpClose = _stmt.getDouble(_cursorIndexOfClose)
          }
          val _tmpVolume: Long?
          if (_stmt.isNull(_cursorIndexOfVolume)) {
            _tmpVolume = null
          } else {
            _tmpVolume = _stmt.getLong(_cursorIndexOfVolume)
          }
          val _tmpAdjustedClose: Double?
          if (_stmt.isNull(_cursorIndexOfAdjustedClose)) {
            _tmpAdjustedClose = null
          } else {
            _tmpAdjustedClose = _stmt.getDouble(_cursorIndexOfAdjustedClose)
          }
          _item =
              PriceHistoryEntity(_tmpCompanyId,_tmpDate,_tmpOpen,_tmpHigh,_tmpLow,_tmpClose,_tmpVolume,_tmpAdjustedClose)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSectorBenchmark(sector: String): SectorBenchmarkEntity? {
    val _sql: String = "SELECT * FROM sector_benchmarks WHERE sector = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, sector)
        val _cursorIndexOfSector: Int = getColumnIndexOrThrow(_stmt, "sector")
        val _cursorIndexOfRoeMedian: Int = getColumnIndexOrThrow(_stmt, "roe_median")
        val _cursorIndexOfRoaMedian: Int = getColumnIndexOrThrow(_stmt, "roa_median")
        val _cursorIndexOfNetMarginMedian: Int = getColumnIndexOrThrow(_stmt, "net_margin_median")
        val _cursorIndexOfCompanyCount: Int = getColumnIndexOrThrow(_stmt, "company_count")
        val _result: SectorBenchmarkEntity?
        if (_stmt.step()) {
          val _tmpSector: String
          _tmpSector = _stmt.getText(_cursorIndexOfSector)
          val _tmpRoeMedian: Double?
          if (_stmt.isNull(_cursorIndexOfRoeMedian)) {
            _tmpRoeMedian = null
          } else {
            _tmpRoeMedian = _stmt.getDouble(_cursorIndexOfRoeMedian)
          }
          val _tmpRoaMedian: Double?
          if (_stmt.isNull(_cursorIndexOfRoaMedian)) {
            _tmpRoaMedian = null
          } else {
            _tmpRoaMedian = _stmt.getDouble(_cursorIndexOfRoaMedian)
          }
          val _tmpNetMarginMedian: Double?
          if (_stmt.isNull(_cursorIndexOfNetMarginMedian)) {
            _tmpNetMarginMedian = null
          } else {
            _tmpNetMarginMedian = _stmt.getDouble(_cursorIndexOfNetMarginMedian)
          }
          val _tmpCompanyCount: Int?
          if (_stmt.isNull(_cursorIndexOfCompanyCount)) {
            _tmpCompanyCount = null
          } else {
            _tmpCompanyCount = _stmt.getLong(_cursorIndexOfCompanyCount).toInt()
          }
          _result =
              SectorBenchmarkEntity(_tmpSector,_tmpRoeMedian,_tmpRoaMedian,_tmpNetMarginMedian,_tmpCompanyCount)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFactorHistory(companyId: Int): List<FactorHistoryEntity> {
    val _sql: String =
        "SELECT * FROM factor_history_quarterly WHERE company_id = ? ORDER BY quarter_end"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, companyId.toLong())
        val _cursorIndexOfCompanyId: Int = getColumnIndexOrThrow(_stmt, "company_id")
        val _cursorIndexOfQuarterEnd: Int = getColumnIndexOrThrow(_stmt, "quarter_end")
        val _cursorIndexOfScoringDate: Int = getColumnIndexOrThrow(_stmt, "scoring_date")
        val _cursorIndexOfBuffett: Int = getColumnIndexOrThrow(_stmt, "buffett")
        val _cursorIndexOfGraham: Int = getColumnIndexOrThrow(_stmt, "graham")
        val _cursorIndexOfPiotroski: Int = getColumnIndexOrThrow(_stmt, "piotroski")
        val _cursorIndexOfMagicFormula: Int = getColumnIndexOrThrow(_stmt, "magic_formula")
        val _cursorIndexOfLynchPeg: Int = getColumnIndexOrThrow(_stmt, "lynch_peg")
        val _cursorIndexOfDcfMos: Int = getColumnIndexOrThrow(_stmt, "dcf_mos")
        val _cursorIndexOfMomentum: Int = getColumnIndexOrThrow(_stmt, "momentum")
        val _cursorIndexOfTechnical: Int = getColumnIndexOrThrow(_stmt, "technical")
        val _cursorIndexOfDividend: Int = getColumnIndexOrThrow(_stmt, "dividend")
        val _cursorIndexOfCompositeAlpha: Int = getColumnIndexOrThrow(_stmt, "composite_alpha")
        val _cursorIndexOfDataCompleteness: Int = getColumnIndexOrThrow(_stmt, "data_completeness")
        val _result: MutableList<FactorHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FactorHistoryEntity
          val _tmpCompanyId: Int
          _tmpCompanyId = _stmt.getLong(_cursorIndexOfCompanyId).toInt()
          val _tmpQuarterEnd: String
          _tmpQuarterEnd = _stmt.getText(_cursorIndexOfQuarterEnd)
          val _tmpScoringDate: String
          _tmpScoringDate = _stmt.getText(_cursorIndexOfScoringDate)
          val _tmpBuffett: Double?
          if (_stmt.isNull(_cursorIndexOfBuffett)) {
            _tmpBuffett = null
          } else {
            _tmpBuffett = _stmt.getDouble(_cursorIndexOfBuffett)
          }
          val _tmpGraham: Double?
          if (_stmt.isNull(_cursorIndexOfGraham)) {
            _tmpGraham = null
          } else {
            _tmpGraham = _stmt.getDouble(_cursorIndexOfGraham)
          }
          val _tmpPiotroski: Double?
          if (_stmt.isNull(_cursorIndexOfPiotroski)) {
            _tmpPiotroski = null
          } else {
            _tmpPiotroski = _stmt.getDouble(_cursorIndexOfPiotroski)
          }
          val _tmpMagicFormula: Double?
          if (_stmt.isNull(_cursorIndexOfMagicFormula)) {
            _tmpMagicFormula = null
          } else {
            _tmpMagicFormula = _stmt.getDouble(_cursorIndexOfMagicFormula)
          }
          val _tmpLynchPeg: Double?
          if (_stmt.isNull(_cursorIndexOfLynchPeg)) {
            _tmpLynchPeg = null
          } else {
            _tmpLynchPeg = _stmt.getDouble(_cursorIndexOfLynchPeg)
          }
          val _tmpDcfMos: Double?
          if (_stmt.isNull(_cursorIndexOfDcfMos)) {
            _tmpDcfMos = null
          } else {
            _tmpDcfMos = _stmt.getDouble(_cursorIndexOfDcfMos)
          }
          val _tmpMomentum: Double?
          if (_stmt.isNull(_cursorIndexOfMomentum)) {
            _tmpMomentum = null
          } else {
            _tmpMomentum = _stmt.getDouble(_cursorIndexOfMomentum)
          }
          val _tmpTechnical: Double?
          if (_stmt.isNull(_cursorIndexOfTechnical)) {
            _tmpTechnical = null
          } else {
            _tmpTechnical = _stmt.getDouble(_cursorIndexOfTechnical)
          }
          val _tmpDividend: Double?
          if (_stmt.isNull(_cursorIndexOfDividend)) {
            _tmpDividend = null
          } else {
            _tmpDividend = _stmt.getDouble(_cursorIndexOfDividend)
          }
          val _tmpCompositeAlpha: Double?
          if (_stmt.isNull(_cursorIndexOfCompositeAlpha)) {
            _tmpCompositeAlpha = null
          } else {
            _tmpCompositeAlpha = _stmt.getDouble(_cursorIndexOfCompositeAlpha)
          }
          val _tmpDataCompleteness: Double?
          if (_stmt.isNull(_cursorIndexOfDataCompleteness)) {
            _tmpDataCompleteness = null
          } else {
            _tmpDataCompleteness = _stmt.getDouble(_cursorIndexOfDataCompleteness)
          }
          _item =
              FactorHistoryEntity(_tmpCompanyId,_tmpQuarterEnd,_tmpScoringDate,_tmpBuffett,_tmpGraham,_tmpPiotroski,_tmpMagicFormula,_tmpLynchPeg,_tmpDcfMos,_tmpMomentum,_tmpTechnical,_tmpDividend,_tmpCompositeAlpha,_tmpDataCompleteness)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeModelPerformance(): Flow<List<ModelPerformanceEntity>> {
    val _sql: String = "SELECT * FROM model_performance ORDER BY date"
    return createFlow(__db, false, arrayOf("model_performance")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _cursorIndexOfStrategyReturn: Int = getColumnIndexOrThrow(_stmt, "strategy_return")
        val _cursorIndexOfBenchmarkReturn: Int = getColumnIndexOrThrow(_stmt, "benchmark_return")
        val _cursorIndexOfAlpha: Int = getColumnIndexOrThrow(_stmt, "alpha")
        val _result: MutableList<ModelPerformanceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ModelPerformanceEntity
          val _tmpDate: String
          _tmpDate = _stmt.getText(_cursorIndexOfDate)
          val _tmpStrategyReturn: Double?
          if (_stmt.isNull(_cursorIndexOfStrategyReturn)) {
            _tmpStrategyReturn = null
          } else {
            _tmpStrategyReturn = _stmt.getDouble(_cursorIndexOfStrategyReturn)
          }
          val _tmpBenchmarkReturn: Double?
          if (_stmt.isNull(_cursorIndexOfBenchmarkReturn)) {
            _tmpBenchmarkReturn = null
          } else {
            _tmpBenchmarkReturn = _stmt.getDouble(_cursorIndexOfBenchmarkReturn)
          }
          val _tmpAlpha: Double?
          if (_stmt.isNull(_cursorIndexOfAlpha)) {
            _tmpAlpha = null
          } else {
            _tmpAlpha = _stmt.getDouble(_cursorIndexOfAlpha)
          }
          _item = ModelPerformanceEntity(_tmpDate,_tmpStrategyReturn,_tmpBenchmarkReturn,_tmpAlpha)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
