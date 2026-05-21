package com.bistpicker.mobile.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Read-only DAO over the snapshot SQLite.
 */
@Dao
interface SnapshotDao {

    // ── Metadata & Home Summary ───────────────────────────────────────

    @Query("SELECT * FROM snapshot_metadata WHERE id = 1")
    fun observeMetadata(): Flow<SnapshotMetadataEntity?>

    @Query("SELECT * FROM snapshot_metadata WHERE id = 1")
    suspend fun getMetadata(): SnapshotMetadataEntity?

    @Query("SELECT * FROM home_summary WHERE id = 1")
    fun observeHomeSummary(): Flow<HomeSummaryEntity?>

    @Query("SELECT * FROM open_positions ORDER BY sort_order")
    fun observeOpenPositions(): Flow<List<OpenPositionEntity>>

    @Query("SELECT * FROM portfolio_history ORDER BY exit_date DESC, sort_order")
    fun observePortfolioHistory(): Flow<List<PortfolioHistoryEntity>>

    @Query("SELECT * FROM scoring_latest WHERE alpha_core_eligible = 1 ORDER BY ranking_score DESC LIMIT :limit")
    fun observeTopScoring(limit: Int): Flow<List<ScoringLatestEntity>>

    // ── Scoring list (Liste tab) ───────────────────────────────────────

    @Query("""
        SELECT * FROM scoring_latest 
        WHERE (:onlyAlphaCore = 0 OR alpha_core_eligible = 1)
          AND (:onlyAlphaX = 0 OR alpha_x_eligible = 1)
          AND (:onlyBist100 = 0 OR is_bist100 = 1)
          AND (:sector IS NULL OR sector = :sector)
          AND (:risk IS NULL OR risk = :risk)
          AND (:minScore IS NULL OR ranking_score >= :minScore)
          AND (:search IS NULL OR ticker LIKE '%' || :search || '%' OR name LIKE '%' || :search || '%')
        ORDER BY ranking_score DESC 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun queryScoring(
        onlyAlphaCore: Int,
        onlyAlphaX: Int,
        onlyBist100: Int,
        sector: String?,
        risk: String?,
        minScore: Double?,
        search: String?,
        limit: Int,
        offset: Int
    ): List<ScoringLatestEntity>

    @Query("""
        SELECT count(*) FROM scoring_latest 
        WHERE (:onlyAlphaCore = 0 OR alpha_core_eligible = 1)
          AND (:onlyAlphaX = 0 OR alpha_x_eligible = 1)
          AND (:onlyBist100 = 0 OR is_bist100 = 1)
          AND (:sector IS NULL OR sector = :sector)
          AND (:risk IS NULL OR risk = :risk)
          AND (:minScore IS NULL OR ranking_score >= :minScore)
          AND (:search IS NULL OR ticker LIKE '%' || :search || '%' OR name LIKE '%' || :search || '%')
    """)
    suspend fun countScoring(
        onlyAlphaCore: Int,
        onlyAlphaX: Int,
        onlyBist100: Int,
        sector: String?,
        risk: String?,
        minScore: Double?,
        search: String?
    ): Int

    @Query("SELECT DISTINCT sector FROM scoring_latest WHERE sector IS NOT NULL ORDER BY sector")
    suspend fun listSectors(): List<String>

    // ── Detail view (Hisse detay) ─────────────────────────────────────

    @Query("SELECT * FROM scoring_latest WHERE ticker = :ticker LIMIT 1")
    suspend fun getScoring(ticker: String): ScoringLatestEntity?

    @Query("SELECT * FROM companies WHERE ticker = :ticker LIMIT 1")
    suspend fun getCompany(ticker: String): CompanyEntity?

    @Query("SELECT * FROM open_positions WHERE ticker = :ticker LIMIT 1")
    suspend fun getOpenPosition(ticker: String): OpenPositionEntity?

    @Query("SELECT * FROM adjusted_metrics_latest WHERE company_id = :companyId LIMIT 1")
    suspend fun getAdjustedMetrics(companyId: Int): AdjustedMetricsEntity?

    @Query("SELECT * FROM price_history_730d WHERE company_id = :companyId ORDER BY date")
    suspend fun getPriceHistory(companyId: Int): List<PriceHistoryEntity>


    @Query("SELECT * FROM sector_benchmarks WHERE sector = :sector LIMIT 1")
    suspend fun getSectorBenchmark(sector: String): SectorBenchmarkEntity?

    @Query("SELECT * FROM factor_history_quarterly WHERE company_id = :companyId ORDER BY quarter_end")
    suspend fun getFactorHistory(companyId: Int): List<FactorHistoryEntity>
}
