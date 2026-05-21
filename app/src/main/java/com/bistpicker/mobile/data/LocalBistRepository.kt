package com.bistpicker.mobile.data

import com.bistpicker.mobile.data.local.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Repository backed by Room.
 */
class LocalBistRepository(
    private val daoProvider: () -> com.bistpicker.mobile.data.local.SnapshotDao,
    private val json: Json
) : BistRepository {

    private fun dao() = daoProvider()

    override fun observeHome(): Flow<HomeData> {
        val home = dao().observeHomeSummary()
        val positions = dao().observeOpenPositions()
        val history = dao().observePortfolioHistory()
        val topScoring = dao().observeTopScoring(limit = 10)
        
        return combine(home, positions, history, topScoring) { homeRow, positionRows, historyRows, topRows ->
            val openTickers = positionRows.map { it.ticker }.toSet()
            val top10Tickers = topRows.map { it.ticker }.toSet()
            
            val suggestions = mutableListOf<SuggestedAction>()
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
                    sells.add(SuggestedAction(pos.ticker, TradeAction.SELL, "Modelden tamamen dustu"))
                } else {
                    holds.add(SuggestedAction(pos.ticker, TradeAction.HOLD, "Sirket hala guclu"))
                }
            }

            val currentTargetCount = holds.size + buys.size
            if (currentTargetCount > 5) {
                val overflowCount = currentTargetCount - 5
                val holdTickersWithRank = holds.map { hold ->
                    val score = topRows.find { it.ticker == hold.ticker }?.rankingScore ?: 0.0
                    hold to score
                }.sortedBy { it.second }

                for (i in 0 until overflowCount) {
                    val weakest = holdTickersWithRank[i].first
                    holds.remove(weakest)
                    sells.add(SuggestedAction(weakest.ticker, TradeAction.SELL, "Yeni firsata yer ac"))
                }
            }

            suggestions.addAll(buys)
            suggestions.addAll(holds)
            suggestions.addAll(sells)

            val now = java.util.Calendar.getInstance()
            val dayOfWeek = now.get(java.util.Calendar.DAY_OF_WEEK)
            val diffToMonday = if (dayOfWeek >= java.util.Calendar.MONDAY) {
                java.util.Calendar.MONDAY - dayOfWeek
            } else {
                -6
            }
            val monday = now.clone() as java.util.Calendar
            monday.add(java.util.Calendar.DAY_OF_YEAR, diffToMonday)
            val sunday = monday.clone() as java.util.Calendar
            sunday.add(java.util.Calendar.DAY_OF_YEAR, 6)
            val sdf = java.text.SimpleDateFormat("dd MMM", java.util.Locale("tr", "TR"))

            HomeData(
                macro = homeRow?.toMacro(),
                cash = homeRow?.toCash(),
                performance = homeRow?.toPerformance(),
                openPositions = positionRows.map { it.toOpenPosition() },
                history = historyRows.map { it.toClosedPosition() },
                suggestions = suggestions,
                weekStart = sdf.format(monday.time),
                weekEnd = sdf.format(sunday.time),
            )
        }
    }

    override fun observeSnapshotInfo(): Flow<SnapshotInfo?> =
        dao().observeMetadata().let { upstream ->
            kotlinx.coroutines.flow.flow {
                upstream.collect { row -> emit(row?.toSnapshotInfo()) }
            }
        }

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
            rows = rows.map { it.toScoringRow() },
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
        val priceHistory = dao().getPriceHistory(score.companyId).map { it.toPricePoint() }
        val factorHistory = dao().getFactorHistory(score.companyId).map { it.toFactorHistoryPoint() }
        
        val sector = score.sector ?: company?.sectorCustom
        val benchmark = if (sector != null) dao().getSectorBenchmark(sector)?.toDomain() else null

        val flags = parseQualityFlags(openPosition?.qualityFlagsJson ?: score.qualityFlagsJson)

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
            financials = metrics?.toFinancialsSnapshot(),
            factorHistory = factorHistory,
            priceHistory = priceHistory,
            openPosition = openPosition?.toOpenPosition(),
            qualityFlags = flags,
            sectorBenchmark = benchmark,
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

    private fun parseQualityFlags(raw: String?): List<String> {
        if (raw.isNullOrBlank()) return emptyList()
        return runCatching { json.decodeFromString<List<String>>(raw) }.getOrElse { emptyList() }
    }

    private fun parseReasons(raw: String?): List<ReasonFactor> {
        if (raw.isNullOrBlank()) return emptyList()
        return runCatching { json.decodeFromString<List<ReasonFactor>>(raw) }.getOrElse { emptyList() }
    }

    private fun OpenPositionEntity.toOpenPosition() = OpenPosition(
        ticker = ticker,
        name = name,
        portfolio = portfolio,
        entryPrice = entryPrice,
        currentPrice = currentPrice,
        pnlPct = pnlPct,
        targetPrice = targetPrice,
        stopLossPrice = stopLossPrice,
        compositeScore = compositeScore,
        daysHeld = daysHeld,
        selectionDate = selectionDate,
        reasons = parseReasons(reasonTopFactorsJson),
        qualityFlags = parseQualityFlags(qualityFlagsJson),
        dcfMosPct = dcfMosPct,
    )

    private fun PortfolioHistoryEntity.toClosedPosition() = ClosedPosition(
        ticker = ticker,
        name = name,
        selectionDate = selectionDate,
        exitDate = exitDate,
        entryPrice = entryPrice,
        exitPrice = exitPrice,
        pnlPct = pnlPct,
        exitReason = exitReason,
        holdingDays = holdingDays,
    )

    private fun ScoringLatestEntity.toScoringRow() = ScoringRow(
        ticker = ticker,
        name = name,
        sector = sector,
        type = type,
        isBist100 = isBist100 == 1,
        rankingScore = rankingScore,
        rankingSource = rankingSource,
        alpha = alpha,
        alphaXScore = alphaXScore,
        alphaXRank = alphaXRank?.toInt(),
        alphaCoreEligible = alphaCoreEligible == 1,
        alphaXEligible = alphaXEligible == 1,
        researchBucket = ResearchBucket.fromString(alphaResearchBucket),
        primaryBlocker = alphaPrimaryBlocker,
        streak = alphaSnapshotStreak,
        risk = RiskTier.fromString(risk),
        dataCompleteness = dataCompleteness,
        above200ma = null,
        technical = technical,
        momentum = momentum,
        buffett = buffett,
        dcfMos = dcfMos,
    )

    private fun AdjustedMetricsEntity.toFinancialsSnapshot() = FinancialsSnapshot(
        periodEnd = periodEnd,
        reportedNetIncome = reportedNetIncome,
        adjustedNetIncome = adjustedNetIncome,
        ownerEarnings = ownerEarnings,
        freeCashFlow = freeCashFlow,
        roeAdjusted = roeAdjusted,
        roaAdjusted = roaAdjusted,
        epsAdjusted = epsAdjusted,
        realEpsGrowthPct = realEpsGrowthPct,
        relatedPartyRevenuePct = relatedPartyRevenuePct,
    )

    private fun PriceHistoryEntity.toPricePoint() = PricePoint(
        date = date, open = open, high = high, low = low, close = close,
        volume = volume, adjustedClose = adjustedClose,
    )

    private fun FactorHistoryEntity.toFactorHistoryPoint() = FactorHistoryPoint(
        quarterEnd = quarterEnd,
        buffett = buffett,
        graham = graham,
        piotroski = piotroski,
        momentum = momentum,
        technical = technical,
        dcfMos = dcfMos,
        compositeAlpha = compositeAlpha,
    )

    private fun SectorBenchmarkEntity.toDomain() = SectorBenchmark(
        sector = sector,
        roeMedian = roeMedian,
        roaMedian = roaMedian,
        netMarginMedian = netMarginMedian,
        companyCount = companyCount,
    )

    private fun HomeSummaryEntity.toMacro() = HomeMacro(
        date = macroDate,
        policyRatePct = policyRatePct,
        cpiYoyPct = cpiYoyPct,
        usdTryRate = usdTryRate,
        regime = regime,
    )

    private fun HomeSummaryEntity.toCash() = HomeCash(
        state = CashState.fromString(cashState),
        cashPct = cashPct,
        daysInState = cashDaysInState,
        targetState = CashState.fromString(cashTargetState),
        notes = cashNotes,
        rawSignal = cashRawSignal,
    )

    private fun HomeSummaryEntity.toPerformance() = HomePerformance(
        totalReturnAvg = totalReturnAvg,
        activeReturnAvg = activeReturnAvg,
        winRate = winRate,
        benchmarkYtd = benchmarkYtd,
    )

    private fun SnapshotMetadataEntity.toSnapshotInfo() = SnapshotInfo(
        schemaVersion = schemaVersion,
        snapshotDate = snapshotDate,
        exportedAt = exportedAt,
        companyCount = companyCount,
        priceHistoryDays = priceHistoryDays,
    )
}
