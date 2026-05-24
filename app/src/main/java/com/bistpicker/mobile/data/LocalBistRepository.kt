package com.bistpicker.mobile.data

import android.content.Context
import com.bistpicker.mobile.data.api.LivePriceClient
import com.bistpicker.mobile.data.local.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*
import java.text.SimpleDateFormat

/**
 * Repository backed by Room.
 */
class LocalBistRepository(
    private val context: Context,
    private val daoProvider: () -> SnapshotDao,
    private val json: Json,
    private val livePriceClient: LivePriceClient? = null
) : BistRepository {

    private val weeklyPerformanceManager = WeeklyPerformanceManager(context, json)

    private fun dao() = daoProvider()

    private val databaseRebuildTrigger = MutableStateFlow(0)

    fun notifyDatabaseRebuilt() {
        databaseRebuildTrigger.update { it + 1 }
    }

    // In-memory live price cache
    private val _livePrices = MutableStateFlow<Map<String, Double>>(emptyMap())
    val livePrices: StateFlow<Map<String, Double>> = _livePrices.asStateFlow()

    override suspend fun refreshLivePrices(tickers: List<String>) {
        if (livePriceClient == null) return
        val targetTickers = (tickers + "XU100").distinct()
        val fresh = livePriceClient.fetchPrices(targetTickers)
        if (fresh.isNotEmpty()) {
            _livePrices.update { current -> current + fresh }
        }
    }

    private fun getCurrentMondayDate(): String {
        val now = Calendar.getInstance()
        val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
        val diffToMonday = if (dayOfWeek >= Calendar.MONDAY) {
            Calendar.MONDAY - dayOfWeek
        } else {
            -6
        }
        val monday = now.clone() as Calendar
        monday.add(Calendar.DAY_OF_YEAR, diffToMonday)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(monday.time)
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun observeHome(): Flow<HomeData> {
        return databaseRebuildTrigger.flatMapLatest {
            val f1 = dao().observeHomeSummary()
            val f2 = dao().observeOpenPositions()
            val f3 = dao().observePortfolioHistory()
            val f4 = dao().observeTopScoring(limit = 10)
            val f5 = dao().observeModelPerformance()
            val f6 = _livePrices
            val f7 = databaseRebuildTrigger.map {
                val mondayDate = getCurrentMondayDate()
                dao().getBist100PriceOnOrBefore(mondayDate) ?: 14029.54
            }

            combine(
                combine(f1, f2, f3) { a, b, c -> Triple(a, b, c) },
                combine(f4, f5, f6) { d, e, f -> Triple(d, e, f) },
                f7
            ) { t1, t2, bist100MondayPrice ->
                val (homeRow, positionRows, historyRows) = t1
                val (topRows, perfRows, prices) = t2

                val suggestions = calculateSuggestions(positionRows, topRows)

                val now = Calendar.getInstance()
                val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
                val diffToMonday = if (dayOfWeek >= Calendar.MONDAY) {
                    Calendar.MONDAY - dayOfWeek
                } else {
                    -6
                }
                val monday = now.clone() as Calendar
                monday.add(Calendar.DAY_OF_YEAR, diffToMonday)
                val sunday = monday.clone() as Calendar
                sunday.add(Calendar.DAY_OF_YEAR, 6)
                val sdf = SimpleDateFormat("dd MMM", Locale("tr", "TR"))

                val openPositions = positionRows.map { entity ->
                    val pos = entity.toDomain(json)
                    val livePrice = prices[pos.ticker]
                    if (livePrice != null) {
                        val entry = pos.entryPrice ?: 1.0
                        pos.copy(
                            currentPrice = livePrice,
                            pnlPct = (livePrice / entry - 1.0) * 100.0,
                            isLive = true
                        )
                    } else pos
                }

                val currentMonday = getCurrentMondayDate()
                val weeklyPerformance = weeklyPerformanceManager.updateActiveWeek(
                    currentMondayDate = currentMonday,
                    dbPositions = openPositions,
                    livePrices = prices,
                    bist100MondayPrice = bist100MondayPrice
                )

                HomeData(
                    macro = homeRow?.let { row ->
                        HomeMacro(
                            date = row.macroDate,
                            policyRatePct = row.policyRatePct,
                            cpiYoyPct = row.cpiYoyPct,
                            usdTryRate = row.usdTryRate,
                            regime = row.regime,
                        )
                    },
                    cash = homeRow?.let { row ->
                        HomeCash(
                            state = CashState.fromString(row.cashState),
                            cashPct = row.cashPct,
                            daysInState = row.cashDaysInState,
                            targetState = CashState.fromString(row.cashTargetState),
                            notes = row.cashNotes,
                            rawSignal = row.cashRawSignal,
                        )
                    },
                    performance = homeRow?.let { row ->
                        HomePerformance(
                            totalReturnAvg = row.totalReturnAvg,
                            activeReturnAvg = row.activeReturnAvg,
                            winRate = row.winRate,
                            benchmarkYtd = row.benchmarkYtd,
                        )
                    },
                    openPositions = openPositions,
                    history = historyRows.map { it.toDomain() },
                    suggestions = suggestions,
                    modelPerformance = perfRows.map { it.toDomain() },
                    weekStart = sdf.format(monday.time),
                    weekEnd = sdf.format(sunday.time),
                    weeklyPerformance = weeklyPerformance,
                )
            }
        }.flowOn(kotlinx.coroutines.Dispatchers.IO)
    }

    private fun calculateSuggestions(
        positionRows: List<OpenPositionEntity>,
        topRows: List<ScoringLatestEntity>
    ): List<SuggestedAction> {
        val openTickers = positionRows.map { it.ticker }.toSet()
        // Use a wider buffer (Top 12) to prevent excessive churn.
        // A stock is only considered "fallen out of model" if it drops below rank 12.
        val top12Tickers = topRows.take(12).map { it.ticker }.toSet()
        
        val buys = mutableListOf<SuggestedAction>()
        val holds = mutableListOf<SuggestedAction>()
        val sells = mutableListOf<SuggestedAction>()

        topRows.take(5).forEach { row ->
            if (row.ticker !in openTickers) {
                buys.add(SuggestedAction(row.ticker, TradeAction.BUY, "Haftalik Model: Yeni giris"))
            }
        }

        positionRows.forEach { pos ->
            if (pos.ticker !in top12Tickers) {
                sells.add(SuggestedAction(pos.ticker, TradeAction.SELL, "Model kriterlerinden tamamen dustu."))
            } else {
                holds.add(SuggestedAction(pos.ticker, TradeAction.HOLD, "Sirket hala guclu, potansiyel devam ediyor."))
            }
        }

        val currentTargetCount = holds.size + buys.size
        if (currentTargetCount > 5) {
            val overflowCount = currentTargetCount - 5
            val holdTickersWithRank = holds.map { hold ->
                val score = topRows.find { it.ticker == hold.ticker }?.rankingScore ?: 0.0
                hold to score
            }.sortedBy { it.second }

            val strongestNewcomer = buys.firstOrNull()?.ticker ?: "yeni firsatlar"

            for (i in 0 until overflowCount) {
                val weakest = holdTickersWithRank[i].first
                holds.remove(weakest)
                sells.add(SuggestedAction(weakest.ticker, TradeAction.SELL, "Hisse guclu ancak $strongestNewcomer'a yer acmak icin feda edildi."))
            }
        }

        return buys + holds + sells
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun observeSnapshotInfo(): Flow<SnapshotInfo?> =
        databaseRebuildTrigger.flatMapLatest {
            dao().observeMetadata().map { it?.toSnapshotInfo() }
        }.flowOn(kotlinx.coroutines.Dispatchers.IO)

    override suspend fun queryScoring(filters: ScoringFilters, page: Int, pageSize: Int): ScoringPage {
        val (alphaCore, alphaX, modelOnly, allMode) = filters.mode.toFlags()
        val allRows = dao().queryScoring(
            onlyAlphaCore = alphaCore,
            onlyAlphaX = alphaX,
            onlyBist100 = if (filters.onlyBist100) 1 else 0,
            sector = filters.sector,
            risk = filters.risk?.takeIf { it != RiskTier.UNKNOWN }?.name,
            minScore = filters.minScore,
            search = filters.search?.uppercase()?.takeIf { it.isNotBlank() },
            limit = 1000,
            offset = 0,
        )
        val filtered = allRows.filter { row ->
            when (filters.mode) {
                ScoringViewMode.RESEARCH -> {
                    val bucket = ResearchBucket.fromString(row.alphaResearchBucket)
                    bucket == ResearchBucket.QualityShadow || bucket == ResearchBucket.FreeFloatShadow || bucket == ResearchBucket.NonCoreResearch
                }
                ScoringViewMode.MODEL -> row.modelScore != null
                else -> true
            }
        }
        val sorted = when (filters.sortBy) {
            ScoringSortOrder.SCORE_DESC -> filtered.sortedByDescending { it.rankingScore ?: 0.0 }
            ScoringSortOrder.SCORE_ASC -> filtered.sortedBy { it.rankingScore ?: 0.0 }
            ScoringSortOrder.TICKER_ASC -> filtered.sortedBy { it.ticker }
            ScoringSortOrder.TICKER_DESC -> filtered.sortedByDescending { it.ticker }
            ScoringSortOrder.RISK_ASC -> filtered.sortedBy { 
                when (RiskTier.fromString(it.risk)) {
                    RiskTier.LOW -> 0
                    RiskTier.MEDIUM -> 1
                    RiskTier.HIGH -> 2
                    RiskTier.UNKNOWN -> 3
                }
            }
            ScoringSortOrder.RISK_DESC -> filtered.sortedByDescending { 
                when (RiskTier.fromString(it.risk)) {
                    RiskTier.LOW -> 0
                    RiskTier.MEDIUM -> 1
                    RiskTier.HIGH -> 2
                    RiskTier.UNKNOWN -> 3
                }
            }
        }
        val total = sorted.size
        val startOffset = page * pageSize
        val endOffset = minOf(startOffset + pageSize, total)
        val pagedList = if (startOffset < total) sorted.subList(startOffset, endOffset) else emptyList()
        return ScoringPage(
            rows = pagedList.map { it.toDomain() },
            totalCount = total,
            hasMore = (page + 1) * pageSize < total,
        )
    }

    override suspend fun loadFilterOptions(): FilterOptions =
        FilterOptions(sectors = dao().listSectors())

    override suspend fun loadDetail(ticker: String): StockDetail? {
        val score = dao().getScoring(ticker) ?: return null
        val company = dao().getCompany(ticker)
        val openPosition = dao().getOpenPosition(ticker)
        val metrics = dao().getAdjustedMetrics(score.companyId)
        val priceHistory = dao().getPriceHistory(score.companyId).map { it.toDomain() }
        val factorHistory = dao().getFactorHistory(score.companyId).map { it.toDomain() }
        
        val sector = score.sector ?: company?.sectorCustom
        val benchmark = if (sector != null) dao().getSectorBenchmark(sector)?.toDomain() else null

        val flags = parseQualityFlags(json, openPosition?.qualityFlagsJson ?: score.qualityFlagsJson)

        // Calculate suggested action for this stock specifically
        val positionRows = dao().observeOpenPositions().first()
        val topRows = dao().observeTopScoring(limit = 10).first()
        val suggestedAction = calculateSuggestions(positionRows, topRows).find { it.ticker == ticker }

        val livePrice = _livePrices.value[ticker]
        val dbClosePrice = priceHistory.lastOrNull()?.close
        val updatedOpenPos = openPosition?.toDomain(json)?.let { pos ->
            if (livePrice != null) {
                val entry = pos.entryPrice ?: 1.0
                pos.copy(
                    currentPrice = livePrice,
                    pnlPct = (livePrice / entry - 1.0) * 100.0,
                    isLive = true
                )
            } else pos
        }

        return StockDetail(
            ticker = score.ticker,
            name = score.name ?: company?.name,
            sector = sector,
            type = score.type ?: company?.companyType,
            isBist100 = score.isBist100 == 1,
            freeFloatPct = company?.freeFloatPct,
            rankingScore = score.rankingScore,
            rankingSource = score.rankingSource,
            alpha = score.alpha,
            alphaXScore = score.alphaXScore,
            alphaXRank = score.alphaXRank?.toInt(),
            researchBucket = ResearchBucket.fromString(score.alphaResearchBucket),
            aiInsight = score.aiInsight,
            alphaReason = score.alphaReason,
            primaryBlocker = score.alphaPrimaryBlocker,
            streak = score.alphaSnapshotStreak,
            risk = RiskTier.fromString(score.risk),
            dataCompleteness = score.dataCompleteness,
            factors = FactorBreakdown(
                buffett = score.buffett,
                graham = score.graham,
                piotroski = score.piotroski,
                piotroskiRaw = score.piotroskiRaw,
                magicFormula = score.magicFormula,
                lynchPeg = score.lynchPeg,
                dcfMos = score.dcfMos,
                momentum = score.momentum,
                technical = score.technical,
                dividend = score.dividend,
            ),
            dcf = DcfDetail(
                intrinsicValue = score.dcfIntrinsicValue,
                growthRatePct = score.dcfGrowthRatePct,
                discountRatePct = score.dcfDiscountRatePct,
                terminalGrowthPct = score.dcfTerminalGrowthPct,
                mosPct = score.dcfMos,
            ),
            financials = metrics?.toDomain(),
            factorHistory = factorHistory,
            priceHistory = priceHistory,
            openPosition = updatedOpenPos,
            qualityFlags = flags,
            sectorBenchmark = benchmark,
            suggestedAction = suggestedAction,
            stopLossPrice = score.stopLossPrice,
            targetPrice = score.targetPrice,
            isLive = livePrice != null,
            currentPrice = livePrice ?: dbClosePrice,
            snapshotPrice = dbClosePrice
        )
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun observeDetail(ticker: String): Flow<StockDetail?> {
        return databaseRebuildTrigger.flatMapLatest {
            val staticDetailFlow = flow {
                emit(loadDetail(ticker))
            }
            combine(staticDetailFlow, _livePrices) { detail, prices ->
                if (detail == null) null
                else {
                    val livePrice = prices[ticker]
                    val dbClosePrice = detail.priceHistory.lastOrNull()?.close
                    
                    val updatedOpenPos = detail.openPosition?.let { pos ->
                        if (livePrice != null) {
                            val entry = pos.entryPrice ?: 1.0
                            pos.copy(
                                currentPrice = livePrice,
                                pnlPct = (livePrice / entry - 1.0) * 100.0,
                                isLive = true
                            )
                        } else pos
                    }

                    detail.copy(
                        isLive = livePrice != null,
                        currentPrice = livePrice ?: dbClosePrice,
                        snapshotPrice = dbClosePrice,
                        openPosition = updatedOpenPos
                    )
                }
            }
        }.flowOn(kotlinx.coroutines.Dispatchers.IO)
    }

    private fun ScoringViewMode.toFlags(): IntArray4 = when (this) {
        ScoringViewMode.ALPHA_CORE -> IntArray4(1, 0, 0, 0)
        ScoringViewMode.ALPHA_X -> IntArray4(0, 1, 0, 0)
        ScoringViewMode.RESEARCH -> IntArray4(0, 0, 0, 0)
        ScoringViewMode.MODEL -> IntArray4(0, 0, 1, 0)
        ScoringViewMode.ALL -> IntArray4(0, 0, 0, 1)
    }

    private data class IntArray4(val a: Int, val b: Int, val c: Int, val d: Int)

    // Helper functions for parsing
    private fun parseQualityFlags(json: Json, raw: String?): List<String> {
        if (raw.isNullOrBlank()) return emptyList()
        return runCatching { json.decodeFromString<List<String>>(raw) }.getOrElse { emptyList() }
    }

    private fun parseReasons(json: Json, raw: String?): List<ReasonFactor> {
        if (raw.isNullOrBlank()) return emptyList()
        return runCatching { json.decodeFromString<List<ReasonFactor>>(raw) }.getOrElse { emptyList() }
    }

    // Entity to Domain Mappings
    private fun OpenPositionEntity.toDomain(json: Json) = OpenPosition(
        ticker = this.ticker,
        name = this.name,
        portfolio = this.portfolio,
        entryPrice = this.entryPrice,
        currentPrice = this.currentPrice,
        snapshotPrice = this.currentPrice,
        pnlPct = this.pnlPct,
        targetPrice = this.targetPrice,
        stopLossPrice = this.stopLossPrice,
        compositeScore = this.compositeScore,
        daysHeld = this.daysHeld,
        selectionDate = this.selectionDate,
        reasons = parseReasons(json, this.reasonTopFactorsJson),
        qualityFlags = parseQualityFlags(json, this.qualityFlagsJson),
        dcfMosPct = this.dcfMosPct
    )

    private fun PortfolioHistoryEntity.toDomain() = ClosedPosition(
        ticker = this.ticker,
        name = this.name,
        selectionDate = this.selectionDate,
        exitDate = this.exitDate,
        entryPrice = this.entryPrice,
        exitPrice = this.exitPrice,
        pnlPct = this.pnlPct,
        exitReason = this.exitReason,
        holdingDays = this.holdingDays
    )

    private fun ModelPerformanceEntity.toDomain() = ModelPerformancePoint(
        date = this.date,
        strategyReturn = this.strategyReturn ?: 0.0,
        benchmarkReturn = this.benchmarkReturn ?: 0.0,
        alpha = this.alpha ?: 0.0
    )

    private fun ScoringLatestEntity.toDomain() = ScoringRow(
        ticker = this.ticker,
        name = this.name,
        sector = this.sector,
        type = this.type,
        isBist100 = this.isBist100 == 1,
        rankingScore = this.rankingScore,
        rankingSource = this.rankingSource,
        alpha = this.alpha,
        alphaXScore = this.alphaXScore,
        alphaXRank = this.alphaXRank?.toInt(),
        alphaCoreEligible = this.alphaCoreEligible == 1,
        alphaXEligible = this.alphaXEligible == 1,
        researchBucket = ResearchBucket.fromString(this.alphaResearchBucket),
        primaryBlocker = this.alphaPrimaryBlocker,
        streak = this.alphaSnapshotStreak,
        risk = RiskTier.fromString(this.risk),
        dataCompleteness = this.dataCompleteness,
        above200ma = null,
        technical = this.technical,
        momentum = this.momentum,
        buffett = this.buffett,
        dcfMos = this.dcfMos
    )

    private fun AdjustedMetricsEntity.toDomain() = FinancialsSnapshot(
        periodEnd = this.periodEnd,
        reportedNetIncome = this.reportedNetIncome,
        adjustedNetIncome = this.adjustedNetIncome,
        monetaryGainLoss = this.monetaryGainLoss,
        ownerEarnings = this.ownerEarnings,
        freeCashFlow = this.freeCashFlow,
        roeAdjusted = this.roeAdjusted,
        roaAdjusted = this.roaAdjusted,
        epsAdjusted = this.epsAdjusted,
        realEpsGrowthPct = this.realEpsGrowthPct,
        relatedPartyRevenuePct = this.relatedPartyRevenuePct
    )

    private fun PriceHistoryEntity.toDomain() = PricePoint(
        date = this.date, open = this.open, high = this.high, low = this.low, close = this.close,
        volume = this.volume, adjustedClose = this.adjustedClose
    )

    private fun FactorHistoryEntity.toDomain() = FactorHistoryPoint(
        quarterEnd = this.quarterEnd,
        buffett = this.buffett,
        graham = this.graham,
        piotroski = this.piotroski,
        momentum = this.momentum,
        technical = this.technical,
        dcfMos = this.dcfMos,
        compositeAlpha = this.compositeAlpha
    )

    private fun SectorBenchmarkEntity.toDomain() = SectorBenchmark(
        sector = this.sector,
        roeMedian = this.roeMedian,
        roaMedian = this.roaMedian,
        netMarginMedian = this.netMarginMedian,
        companyCount = this.companyCount
    )

    private fun SnapshotMetadataEntity.toSnapshotInfo() = SnapshotInfo(
        schemaVersion = this.schemaVersion,
        snapshotDate = this.snapshotDate,
        exportedAt = this.exportedAt,
        companyCount = this.companyCount,
        priceHistoryDays = this.priceHistoryDays
    )
}
