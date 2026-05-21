package com.bistpicker.mobile.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SnapshotDatabase_Impl : SnapshotDatabase() {
  private val _snapshotDao: Lazy<SnapshotDao> = lazy {
    SnapshotDao_Impl(this)
  }


  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(10,
        "8a2f316c5007cf64db479ccbae637311", "c66d43d0fd5dffec7d166edd51a7fb5a") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `snapshot_metadata` (`id` INTEGER NOT NULL, `schema_version` INTEGER NOT NULL, `exported_at` TEXT NOT NULL, `snapshot_date` TEXT, `latest_price_date` TEXT, `source_db_path` TEXT, `company_count` INTEGER NOT NULL, `scoring_row_count` INTEGER NOT NULL, `price_history_days` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `home_summary` (`id` INTEGER NOT NULL, `total_return_avg` REAL, `active_return_avg` REAL, `win_rate` REAL, `benchmark_ytd` REAL, `macro_date` TEXT, `policy_rate_pct` REAL, `cpi_yoy_pct` REAL, `usdtry_rate` REAL, `regime` TEXT, `cash_state` TEXT, `cash_pct` REAL, `cash_days_in_state` INTEGER, `cash_last_transition_date` TEXT, `cash_target_state` TEXT, `cash_notes` TEXT, `cash_raw_signal` INTEGER, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `open_positions` (`sort_order` INTEGER NOT NULL, `portfolio` TEXT, `ticker` TEXT NOT NULL, `name` TEXT, `company_id` INTEGER, `entry_price` REAL, `current_price` REAL, `pnl_pct` REAL, `target_price` REAL, `stop_loss_price` REAL, `stop_pct_from_entry` REAL, `composite_score` REAL, `selection_date` TEXT, `days_held` INTEGER, `reason_top_factors_json` TEXT, `quality_flags_json` TEXT, `dcf_margin_of_safety_pct` REAL, `dcf_intrinsic_value` REAL, `dcf_growth_rate_pct` REAL, `dcf_discount_rate_pct` REAL, `dcf_terminal_growth_pct` REAL, PRIMARY KEY(`sort_order`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `portfolio_history` (`sort_order` INTEGER NOT NULL, `portfolio` TEXT, `ticker` TEXT NOT NULL, `name` TEXT, `selection_date` TEXT, `exit_date` TEXT, `entry_price` REAL, `exit_price` REAL, `pnl_pct` REAL, `exit_reason` TEXT, `holding_days` INTEGER, PRIMARY KEY(`sort_order`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `companies` (`id` INTEGER NOT NULL, `ticker` TEXT NOT NULL, `name` TEXT, `company_type` TEXT, `sector_bist` TEXT, `sector_custom` TEXT, `is_bist100` INTEGER NOT NULL, `is_ipo` INTEGER NOT NULL, `free_float_pct` REAL, `listing_date` TEXT, `is_active` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `scoring_latest` (`company_id` INTEGER NOT NULL, `ticker` TEXT NOT NULL, `name` TEXT, `sector` TEXT, `type` TEXT, `is_bist100` INTEGER NOT NULL, `is_active` INTEGER NOT NULL, `free_float_pct` REAL, `avg_volume_try` REAL, `ranking_score` REAL, `ranking_source` TEXT, `model_score` REAL, `alpha` REAL, `alpha_x_score` REAL, `alpha_x_rank` REAL, `alpha_x_confidence` REAL, `alpha_core_eligible` INTEGER NOT NULL, `alpha_x_eligible` INTEGER NOT NULL, `alpha_reason` TEXT, `alpha_primary_blocker` TEXT, `alpha_research_bucket` TEXT, `alpha_snapshot_streak` INTEGER, `ai_insight` TEXT, `risk` TEXT, `data_completeness` REAL, `scoring_date` TEXT, `model_used` TEXT, `buffett` REAL, `graham` REAL, `piotroski` REAL, `piotroski_raw` INTEGER, `magic_formula` REAL, `lynch_peg` REAL, `dcf_mos` REAL, `momentum` REAL, `insider` REAL, `technical` REAL, `dividend` REAL, `beta` REAL, `delta` REAL, `quality_flags_json` TEXT, `dcf_intrinsic_value` REAL, `dcf_growth_rate_pct` REAL, `dcf_discount_rate_pct` REAL, `dcf_terminal_growth_pct` REAL, PRIMARY KEY(`company_id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sector_benchmarks` (`sector` TEXT NOT NULL, `roe_median` REAL, `roa_median` REAL, `net_margin_median` REAL, `company_count` INTEGER, PRIMARY KEY(`sector`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `adjusted_metrics_latest` (`company_id` INTEGER NOT NULL, `period_end` TEXT, `reported_net_income` REAL, `monetary_gain_loss` REAL, `adjusted_net_income` REAL, `owner_earnings` REAL, `free_cash_flow` REAL, `roe_adjusted` REAL, `roa_adjusted` REAL, `eps_adjusted` REAL, `real_eps_growth_pct` REAL, `related_party_revenue_pct` REAL, `maintenance_capex` REAL, `growth_capex` REAL, PRIMARY KEY(`company_id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `price_history_730d` (`company_id` INTEGER NOT NULL, `date` TEXT NOT NULL, `open` REAL, `high` REAL, `low` REAL, `close` REAL, `volume` INTEGER, `adjusted_close` REAL, PRIMARY KEY(`company_id`, `date`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `factor_history_quarterly` (`company_id` INTEGER NOT NULL, `quarter_end` TEXT NOT NULL, `scoring_date` TEXT NOT NULL, `buffett` REAL, `graham` REAL, `piotroski` REAL, `magic_formula` REAL, `lynch_peg` REAL, `dcf_mos` REAL, `momentum` REAL, `technical` REAL, `dividend` REAL, `composite_alpha` REAL, `data_completeness` REAL, PRIMARY KEY(`company_id`, `quarter_end`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8a2f316c5007cf64db479ccbae637311')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `snapshot_metadata`")
        connection.execSQL("DROP TABLE IF EXISTS `home_summary`")
        connection.execSQL("DROP TABLE IF EXISTS `open_positions`")
        connection.execSQL("DROP TABLE IF EXISTS `portfolio_history`")
        connection.execSQL("DROP TABLE IF EXISTS `companies`")
        connection.execSQL("DROP TABLE IF EXISTS `scoring_latest`")
        connection.execSQL("DROP TABLE IF EXISTS `sector_benchmarks`")
        connection.execSQL("DROP TABLE IF EXISTS `adjusted_metrics_latest`")
        connection.execSQL("DROP TABLE IF EXISTS `price_history_730d`")
        connection.execSQL("DROP TABLE IF EXISTS `factor_history_quarterly`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsSnapshotMetadata: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSnapshotMetadata.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSnapshotMetadata.put("schema_version", TableInfo.Column("schema_version", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSnapshotMetadata.put("exported_at", TableInfo.Column("exported_at", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSnapshotMetadata.put("snapshot_date", TableInfo.Column("snapshot_date", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSnapshotMetadata.put("latest_price_date", TableInfo.Column("latest_price_date",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSnapshotMetadata.put("source_db_path", TableInfo.Column("source_db_path", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSnapshotMetadata.put("company_count", TableInfo.Column("company_count", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSnapshotMetadata.put("scoring_row_count", TableInfo.Column("scoring_row_count",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSnapshotMetadata.put("price_history_days", TableInfo.Column("price_history_days",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSnapshotMetadata: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSnapshotMetadata: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSnapshotMetadata: TableInfo = TableInfo("snapshot_metadata",
            _columnsSnapshotMetadata, _foreignKeysSnapshotMetadata, _indicesSnapshotMetadata)
        val _existingSnapshotMetadata: TableInfo = read(connection, "snapshot_metadata")
        if (!_infoSnapshotMetadata.equals(_existingSnapshotMetadata)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |snapshot_metadata(com.bistpicker.mobile.data.local.SnapshotMetadataEntity).
              | Expected:
              |""".trimMargin() + _infoSnapshotMetadata + """
              |
              | Found:
              |""".trimMargin() + _existingSnapshotMetadata)
        }
        val _columnsHomeSummary: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsHomeSummary.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("total_return_avg", TableInfo.Column("total_return_avg", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("active_return_avg", TableInfo.Column("active_return_avg", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("win_rate", TableInfo.Column("win_rate", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("benchmark_ytd", TableInfo.Column("benchmark_ytd", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("macro_date", TableInfo.Column("macro_date", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("policy_rate_pct", TableInfo.Column("policy_rate_pct", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("cpi_yoy_pct", TableInfo.Column("cpi_yoy_pct", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("usdtry_rate", TableInfo.Column("usdtry_rate", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("regime", TableInfo.Column("regime", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("cash_state", TableInfo.Column("cash_state", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("cash_pct", TableInfo.Column("cash_pct", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("cash_days_in_state", TableInfo.Column("cash_days_in_state",
            "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("cash_last_transition_date",
            TableInfo.Column("cash_last_transition_date", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("cash_target_state", TableInfo.Column("cash_target_state", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("cash_notes", TableInfo.Column("cash_notes", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHomeSummary.put("cash_raw_signal", TableInfo.Column("cash_raw_signal", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysHomeSummary: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesHomeSummary: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoHomeSummary: TableInfo = TableInfo("home_summary", _columnsHomeSummary,
            _foreignKeysHomeSummary, _indicesHomeSummary)
        val _existingHomeSummary: TableInfo = read(connection, "home_summary")
        if (!_infoHomeSummary.equals(_existingHomeSummary)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |home_summary(com.bistpicker.mobile.data.local.HomeSummaryEntity).
              | Expected:
              |""".trimMargin() + _infoHomeSummary + """
              |
              | Found:
              |""".trimMargin() + _existingHomeSummary)
        }
        val _columnsOpenPositions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsOpenPositions.put("sort_order", TableInfo.Column("sort_order", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("portfolio", TableInfo.Column("portfolio", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("ticker", TableInfo.Column("ticker", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("name", TableInfo.Column("name", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("company_id", TableInfo.Column("company_id", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("entry_price", TableInfo.Column("entry_price", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("current_price", TableInfo.Column("current_price", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("pnl_pct", TableInfo.Column("pnl_pct", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("target_price", TableInfo.Column("target_price", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("stop_loss_price", TableInfo.Column("stop_loss_price", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("stop_pct_from_entry", TableInfo.Column("stop_pct_from_entry",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("composite_score", TableInfo.Column("composite_score", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("selection_date", TableInfo.Column("selection_date", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("days_held", TableInfo.Column("days_held", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("reason_top_factors_json",
            TableInfo.Column("reason_top_factors_json", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("quality_flags_json", TableInfo.Column("quality_flags_json",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("dcf_margin_of_safety_pct",
            TableInfo.Column("dcf_margin_of_safety_pct", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("dcf_intrinsic_value", TableInfo.Column("dcf_intrinsic_value",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("dcf_growth_rate_pct", TableInfo.Column("dcf_growth_rate_pct",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("dcf_discount_rate_pct", TableInfo.Column("dcf_discount_rate_pct",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOpenPositions.put("dcf_terminal_growth_pct",
            TableInfo.Column("dcf_terminal_growth_pct", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysOpenPositions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesOpenPositions: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoOpenPositions: TableInfo = TableInfo("open_positions", _columnsOpenPositions,
            _foreignKeysOpenPositions, _indicesOpenPositions)
        val _existingOpenPositions: TableInfo = read(connection, "open_positions")
        if (!_infoOpenPositions.equals(_existingOpenPositions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |open_positions(com.bistpicker.mobile.data.local.OpenPositionEntity).
              | Expected:
              |""".trimMargin() + _infoOpenPositions + """
              |
              | Found:
              |""".trimMargin() + _existingOpenPositions)
        }
        val _columnsPortfolioHistory: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPortfolioHistory.put("sort_order", TableInfo.Column("sort_order", "INTEGER", true,
            1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("portfolio", TableInfo.Column("portfolio", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("ticker", TableInfo.Column("ticker", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("name", TableInfo.Column("name", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("selection_date", TableInfo.Column("selection_date", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("exit_date", TableInfo.Column("exit_date", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("entry_price", TableInfo.Column("entry_price", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("exit_price", TableInfo.Column("exit_price", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("pnl_pct", TableInfo.Column("pnl_pct", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("exit_reason", TableInfo.Column("exit_reason", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPortfolioHistory.put("holding_days", TableInfo.Column("holding_days", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPortfolioHistory: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPortfolioHistory: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPortfolioHistory: TableInfo = TableInfo("portfolio_history",
            _columnsPortfolioHistory, _foreignKeysPortfolioHistory, _indicesPortfolioHistory)
        val _existingPortfolioHistory: TableInfo = read(connection, "portfolio_history")
        if (!_infoPortfolioHistory.equals(_existingPortfolioHistory)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |portfolio_history(com.bistpicker.mobile.data.local.PortfolioHistoryEntity).
              | Expected:
              |""".trimMargin() + _infoPortfolioHistory + """
              |
              | Found:
              |""".trimMargin() + _existingPortfolioHistory)
        }
        val _columnsCompanies: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCompanies.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("ticker", TableInfo.Column("ticker", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("name", TableInfo.Column("name", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("company_type", TableInfo.Column("company_type", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("sector_bist", TableInfo.Column("sector_bist", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("sector_custom", TableInfo.Column("sector_custom", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("is_bist100", TableInfo.Column("is_bist100", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("is_ipo", TableInfo.Column("is_ipo", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("free_float_pct", TableInfo.Column("free_float_pct", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("listing_date", TableInfo.Column("listing_date", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCompanies.put("is_active", TableInfo.Column("is_active", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCompanies: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCompanies: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCompanies: TableInfo = TableInfo("companies", _columnsCompanies,
            _foreignKeysCompanies, _indicesCompanies)
        val _existingCompanies: TableInfo = read(connection, "companies")
        if (!_infoCompanies.equals(_existingCompanies)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |companies(com.bistpicker.mobile.data.local.CompanyEntity).
              | Expected:
              |""".trimMargin() + _infoCompanies + """
              |
              | Found:
              |""".trimMargin() + _existingCompanies)
        }
        val _columnsScoringLatest: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsScoringLatest.put("company_id", TableInfo.Column("company_id", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("ticker", TableInfo.Column("ticker", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("name", TableInfo.Column("name", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("sector", TableInfo.Column("sector", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("type", TableInfo.Column("type", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("is_bist100", TableInfo.Column("is_bist100", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("is_active", TableInfo.Column("is_active", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("free_float_pct", TableInfo.Column("free_float_pct", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("avg_volume_try", TableInfo.Column("avg_volume_try", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("ranking_score", TableInfo.Column("ranking_score", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("ranking_source", TableInfo.Column("ranking_source", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("model_score", TableInfo.Column("model_score", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha", TableInfo.Column("alpha", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_x_score", TableInfo.Column("alpha_x_score", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_x_rank", TableInfo.Column("alpha_x_rank", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_x_confidence", TableInfo.Column("alpha_x_confidence",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_core_eligible", TableInfo.Column("alpha_core_eligible",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_x_eligible", TableInfo.Column("alpha_x_eligible",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_reason", TableInfo.Column("alpha_reason", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_primary_blocker", TableInfo.Column("alpha_primary_blocker",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_research_bucket", TableInfo.Column("alpha_research_bucket",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("alpha_snapshot_streak", TableInfo.Column("alpha_snapshot_streak",
            "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("ai_insight", TableInfo.Column("ai_insight", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("risk", TableInfo.Column("risk", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("data_completeness", TableInfo.Column("data_completeness", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("scoring_date", TableInfo.Column("scoring_date", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("model_used", TableInfo.Column("model_used", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("buffett", TableInfo.Column("buffett", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("graham", TableInfo.Column("graham", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("piotroski", TableInfo.Column("piotroski", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("piotroski_raw", TableInfo.Column("piotroski_raw", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("magic_formula", TableInfo.Column("magic_formula", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("lynch_peg", TableInfo.Column("lynch_peg", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("dcf_mos", TableInfo.Column("dcf_mos", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("momentum", TableInfo.Column("momentum", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("insider", TableInfo.Column("insider", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("technical", TableInfo.Column("technical", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("dividend", TableInfo.Column("dividend", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("beta", TableInfo.Column("beta", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("delta", TableInfo.Column("delta", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("quality_flags_json", TableInfo.Column("quality_flags_json",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("dcf_intrinsic_value", TableInfo.Column("dcf_intrinsic_value",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("dcf_growth_rate_pct", TableInfo.Column("dcf_growth_rate_pct",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("dcf_discount_rate_pct", TableInfo.Column("dcf_discount_rate_pct",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScoringLatest.put("dcf_terminal_growth_pct",
            TableInfo.Column("dcf_terminal_growth_pct", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysScoringLatest: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesScoringLatest: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoScoringLatest: TableInfo = TableInfo("scoring_latest", _columnsScoringLatest,
            _foreignKeysScoringLatest, _indicesScoringLatest)
        val _existingScoringLatest: TableInfo = read(connection, "scoring_latest")
        if (!_infoScoringLatest.equals(_existingScoringLatest)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |scoring_latest(com.bistpicker.mobile.data.local.ScoringLatestEntity).
              | Expected:
              |""".trimMargin() + _infoScoringLatest + """
              |
              | Found:
              |""".trimMargin() + _existingScoringLatest)
        }
        val _columnsSectorBenchmarks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSectorBenchmarks.put("sector", TableInfo.Column("sector", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSectorBenchmarks.put("roe_median", TableInfo.Column("roe_median", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSectorBenchmarks.put("roa_median", TableInfo.Column("roa_median", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSectorBenchmarks.put("net_margin_median", TableInfo.Column("net_margin_median",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSectorBenchmarks.put("company_count", TableInfo.Column("company_count", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSectorBenchmarks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSectorBenchmarks: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSectorBenchmarks: TableInfo = TableInfo("sector_benchmarks",
            _columnsSectorBenchmarks, _foreignKeysSectorBenchmarks, _indicesSectorBenchmarks)
        val _existingSectorBenchmarks: TableInfo = read(connection, "sector_benchmarks")
        if (!_infoSectorBenchmarks.equals(_existingSectorBenchmarks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sector_benchmarks(com.bistpicker.mobile.data.local.SectorBenchmarkEntity).
              | Expected:
              |""".trimMargin() + _infoSectorBenchmarks + """
              |
              | Found:
              |""".trimMargin() + _existingSectorBenchmarks)
        }
        val _columnsAdjustedMetricsLatest: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAdjustedMetricsLatest.put("company_id", TableInfo.Column("company_id", "INTEGER",
            true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("period_end", TableInfo.Column("period_end", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("reported_net_income",
            TableInfo.Column("reported_net_income", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("monetary_gain_loss",
            TableInfo.Column("monetary_gain_loss", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("adjusted_net_income",
            TableInfo.Column("adjusted_net_income", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("owner_earnings", TableInfo.Column("owner_earnings",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("free_cash_flow", TableInfo.Column("free_cash_flow",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("roe_adjusted", TableInfo.Column("roe_adjusted", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("roa_adjusted", TableInfo.Column("roa_adjusted", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("eps_adjusted", TableInfo.Column("eps_adjusted", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("real_eps_growth_pct",
            TableInfo.Column("real_eps_growth_pct", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("related_party_revenue_pct",
            TableInfo.Column("related_party_revenue_pct", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("maintenance_capex", TableInfo.Column("maintenance_capex",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAdjustedMetricsLatest.put("growth_capex", TableInfo.Column("growth_capex", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAdjustedMetricsLatest: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAdjustedMetricsLatest: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAdjustedMetricsLatest: TableInfo = TableInfo("adjusted_metrics_latest",
            _columnsAdjustedMetricsLatest, _foreignKeysAdjustedMetricsLatest,
            _indicesAdjustedMetricsLatest)
        val _existingAdjustedMetricsLatest: TableInfo = read(connection, "adjusted_metrics_latest")
        if (!_infoAdjustedMetricsLatest.equals(_existingAdjustedMetricsLatest)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |adjusted_metrics_latest(com.bistpicker.mobile.data.local.AdjustedMetricsEntity).
              | Expected:
              |""".trimMargin() + _infoAdjustedMetricsLatest + """
              |
              | Found:
              |""".trimMargin() + _existingAdjustedMetricsLatest)
        }
        val _columnsPriceHistory730d: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPriceHistory730d.put("company_id", TableInfo.Column("company_id", "INTEGER", true,
            1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPriceHistory730d.put("date", TableInfo.Column("date", "TEXT", true, 2, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPriceHistory730d.put("open", TableInfo.Column("open", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPriceHistory730d.put("high", TableInfo.Column("high", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPriceHistory730d.put("low", TableInfo.Column("low", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPriceHistory730d.put("close", TableInfo.Column("close", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPriceHistory730d.put("volume", TableInfo.Column("volume", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPriceHistory730d.put("adjusted_close", TableInfo.Column("adjusted_close", "REAL",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPriceHistory730d: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPriceHistory730d: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPriceHistory730d: TableInfo = TableInfo("price_history_730d",
            _columnsPriceHistory730d, _foreignKeysPriceHistory730d, _indicesPriceHistory730d)
        val _existingPriceHistory730d: TableInfo = read(connection, "price_history_730d")
        if (!_infoPriceHistory730d.equals(_existingPriceHistory730d)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |price_history_730d(com.bistpicker.mobile.data.local.PriceHistoryEntity).
              | Expected:
              |""".trimMargin() + _infoPriceHistory730d + """
              |
              | Found:
              |""".trimMargin() + _existingPriceHistory730d)
        }
        val _columnsFactorHistoryQuarterly: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFactorHistoryQuarterly.put("company_id", TableInfo.Column("company_id", "INTEGER",
            true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("quarter_end", TableInfo.Column("quarter_end", "TEXT",
            true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("scoring_date", TableInfo.Column("scoring_date", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("buffett", TableInfo.Column("buffett", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("graham", TableInfo.Column("graham", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("piotroski", TableInfo.Column("piotroski", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("magic_formula", TableInfo.Column("magic_formula",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("lynch_peg", TableInfo.Column("lynch_peg", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("dcf_mos", TableInfo.Column("dcf_mos", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("momentum", TableInfo.Column("momentum", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("technical", TableInfo.Column("technical", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("dividend", TableInfo.Column("dividend", "REAL", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("composite_alpha", TableInfo.Column("composite_alpha",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactorHistoryQuarterly.put("data_completeness",
            TableInfo.Column("data_completeness", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFactorHistoryQuarterly: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFactorHistoryQuarterly: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoFactorHistoryQuarterly: TableInfo = TableInfo("factor_history_quarterly",
            _columnsFactorHistoryQuarterly, _foreignKeysFactorHistoryQuarterly,
            _indicesFactorHistoryQuarterly)
        val _existingFactorHistoryQuarterly: TableInfo = read(connection,
            "factor_history_quarterly")
        if (!_infoFactorHistoryQuarterly.equals(_existingFactorHistoryQuarterly)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |factor_history_quarterly(com.bistpicker.mobile.data.local.FactorHistoryEntity).
              | Expected:
              |""".trimMargin() + _infoFactorHistoryQuarterly + """
              |
              | Found:
              |""".trimMargin() + _existingFactorHistoryQuarterly)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "snapshot_metadata",
        "home_summary", "open_positions", "portfolio_history", "companies", "scoring_latest",
        "sector_benchmarks", "adjusted_metrics_latest", "price_history_730d",
        "factor_history_quarterly")
  }

  public override fun clearAllTables() {
    super.performClear(false, "snapshot_metadata", "home_summary", "open_positions",
        "portfolio_history", "companies", "scoring_latest", "sector_benchmarks",
        "adjusted_metrics_latest", "price_history_730d", "factor_history_quarterly")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(SnapshotDao::class, SnapshotDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun snapshotDao(): SnapshotDao = _snapshotDao.value
}
