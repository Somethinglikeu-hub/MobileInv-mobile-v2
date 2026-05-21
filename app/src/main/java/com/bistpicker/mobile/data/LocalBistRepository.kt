package com.bistpicker.mobile.data

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
    private val daoProvider: () -> SnapshotDao,
    private val json: Json,
    private val livePriceClient: LivePriceClient? = null
) : BistRepository {

    private fun dao() = daoProvider()

    // In-memory live price cache
    private val _livePrices = MutableStateFlow<Map<String, Double>>(emptyMap())
    val livePrices: StateFlow<Map<String, Double>> = _livePrices.asStateFlow()

    override suspend fun refreshLivePrices(tickers: List<String>) {
        if (livePriceClient == null) return
        val fresh = livePriceClient.fetchPrices(tickers)
        if (fresh.isNotEmpty()) {
            _livePrices.update { current -> current + fresh }
        }
    }

    override fun observeHome(): Flow<HomeData> {
        val f1 = dao().observeHomeSummary()
        val f2 = dao().observeOpenPositions()
        val f3 = dao().observePortfolioHistory()
        val f4 = dao().observeTopScoring(limit = 10)
        val f5 = dao().observeModelPerformance()
        val f6 = _livePrices

        return combine(
            combine(f1, f2, f3) { a, b, c -> Triple(a, b, c) },
            combine(f4, f5, f6) { d, e, f -> Triple(d, e, f) }
        ) { t1, t2 ->
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
                openPositions = positionRows.map { entity ->
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
                },
                history = historyRows.map { it.toDomain() },
                suggestions = suggestions,
                modelPerformance = perfRows.map { it.toDomain() },
                weekStart = sdf.format(monday.time),
                weekEnd = sdf.format(sunday.time),
            )
        }
    }

    private fun calculateSuggestions(
        positionRows: List<OpenPositionEntity>,
        topRows: List<ScoringLatestEntity>
    ): List<SuggestedAction> {
        val openTickers = positionRows.map { it.ticker }.toSet()
        val top10Tickers = topRows.map { it.ticker }.toSet()
        
        val buys = mutableListOf<SuggestedAction>()
        val holds = mutableListOf<SuggestedAction>()
        val sells = mutableListOf<SuggestedAction>()

        topRows.take(5).forEach { row ->
            if (row.ticker !in openTickers) {
                buys.add(SuggestedAction(row.ticker, TradeAction.BUY, "Haftalik Model: Yeni giris"))
            }
        }

        positionRows.forEach { pos ->
            if (pos.ticker !in top10Tickers) {
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

    override fun observeSnapshotInfo(): Flow<SnapshotInfo?> =
        dao().observeMetadata().map { it?.toSnapshotInfo() }

    override suspend fun queryScoring(filters: ScoringFilters, page: Int, pageSize: Int): ScoringPage {
        val (alphaCore, alphaX, modelOnly, allMode) = filters.mode.toFlags()
        val rows = dao().queryScoring(
            onlyAlphaCore = alphaCore,
            onlyAlphaX = alphaX,
            onlyBist100 = if (filters.onlyBist100) 1 else 0,
            sector = filters.sector,
            risk = filters.risk?.takeIf { it != RiskTier.UNKNOWN }?.name,
            minScore = filters.minScore,
            search = filters.search?.uppercase()?.takeIf { it.isNotBlank() },
            limit = pageSize,
            offset = page * pageSize,
        ).filter { row ->
            when (filters.mode) {
                ScoringViewMode.RESEARCH -> {
                    val bucket = ResearchBucket.fromString(row.alphaResearchBucket)
                    bucket == ResearchBucket.QualityShadow || bucket == ResearchBucket.FreeFloatShadow || bucket == ResearchBucket.NonCoreResearch
                }
                ScoringViewMode.MODEL -> row.modelScore != null
                else -> true
            }
        }
        val total = dao().countScoring(
            onlyAlphaCore = alphaCore,
            onlyAlphaX = alphaX,
            onlyBist100 = if (filters.onlyBist100) 1 else 0,
            sector = filters.sector,
            risk = filters.risk?.takeIf { it != RiskTier.UNKNOWN }?.name,
            minScore = filters.minScore,
            search = filters.search?.uppercase()?.takeIf { it.isNotBlank() },
        )
        return ScoringPage(
            rows = rows.map { it.toDomain() },
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
            openPosition = openPosition?.toDomain(json),
            qualityFlags = flags,
            sectorBenchmark = benchmark,
            suggestedAction = suggestedAction,
        )
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
