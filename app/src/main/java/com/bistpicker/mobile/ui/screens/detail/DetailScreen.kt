package com.bistpicker.mobile.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bistpicker.mobile.AppContainerProvider
import com.bistpicker.mobile.data.SectorBenchmark
import com.bistpicker.mobile.data.StockDetail
import com.bistpicker.mobile.data.TradeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    ticker: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(
        key = ticker,
        factory = DetailViewModel.Factory(ticker, AppContainerProvider.get(LocalContext.current).repository)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = ticker, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding).fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
            when (val state = uiState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is DetailUiState.Success -> {
                    DetailContent(state.detail)
                }
                is DetailUiState.Error -> {
                    Text(text = state.message, modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun DetailContent(detail: StockDetail) {
    val isInPortfolio = detail.openPosition != null

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Header with Status Badge & Smart Signal
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = detail.ticker, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    if (isInPortfolio) {
                        Spacer(Modifier.width(12.dp))
                        Surface(color = Color(0xFF4CAF50), shape = MaterialTheme.shapes.small) {
                            Text("PORTFOYDE", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Text(text = detail.name ?: "", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.outline)
                
                // Smart Signal Reason (if any)
                detail.suggestedAction?.let { action ->
                    if (action.action == TradeAction.SELL) {
                        Spacer(Modifier.height(12.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f))
                        ) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("ZEKI SINYAL: SAT", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
                                Spacer(Modifier.width(12.dp))
                                Text(action.reason ?: "", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                            }
                        }
                    }
                }
            }
        }

        // 2. Investment Thesis / Inclusion Reason
        item {
            val sectionTitle = if (isInPortfolio) "Neden Portfoyde?" else "Yatirim Tezi (Analiz)"
            Text(text = sectionTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val f = detail.factors
                    if ((f.buffett ?: 0.0) > 75) {
                        InclusionReason(
                            "Kalite Odakli Buyume (Buffett)",
                            "Sirket, yuksek ozsermaye karliligi ile yatirdigi her 1 TL'yi verimli bir sekilde buyutuyor. Rekabet avantaji (Moat) cok guclu."
                        )
                    }
                    if ((f.graham ?: 0.0) > 75) {
                        InclusionReason(
                            "Benjamin Graham Iskontosu",
                            "Hisse fiyati, sirketin sahip oldugu varliklarin ve reel faiz ortamindaki degerinin cok altinda. Guvenlik marji (MOS) yuksek."
                        )
                    }
                    if ((f.momentum ?: 0.0) > 75) {
                        InclusionReason(
                            "Trend Takibi (Momentum)",
                            "Piyasa bu sirketi fark etmis durumda. Yukselis ivmesi teknik olarak cok guclu ve ana endeksin uzerinde getiri sagliyor."
                        )
                    }
                    
                    if (isInPortfolio && detail.openPosition?.reasons?.isNotEmpty() == true) {
                        HorizontalDivider(Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        detail.openPosition.reasons.forEach { reason ->
                            Text(text = "• ${reason.label}: ${String.format("%.1f", reason.value)}", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    detail.aiInsight?.let { insight ->
                        HorizontalDivider(Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("AI Analiz: ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            Text(text = insight, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // 3. Deep Financial Insights
        item {
            Text(text = "Derin Finansal Analiz", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val fin = detail.financials
                
                InsightMetricCard(
                    label = "ROE (Ozkaynak Karliligi)",
                    value = "${String.format("%.1f", (fin?.roeAdjusted ?: 0.0) * 100)}%",
                    explanation = "Sirket ortaklarinin koydugu sermayeyi yillik %${String.format("%.1f", (fin?.roeAdjusted ?: 0.0) * 100)} oraninda karla isletiyor. %20 uzeri genellikle 'basarili' kabul edilir.",
                    benchmark = detail.sectorBenchmark?.roeMedian,
                    benchmarkLabel = "Sektor Medyani"
                )

                InsightMetricCard(
                    label = "Serbest Nakit Akisi (FCF)",
                    value = "${String.format("%.0f", (fin?.freeCashFlow ?: 0.0) / 1_000_000.0)} M TL",
                    explanation = "Tüm yatirimlar yapildiktan sonra sirketin elinde kalan net nakit. Bu rakam ne kadar yuksekse temettu ve yeni yatirim potansiyeli o kadar artar."
                )

                InsightMetricCard(
                    label = "Reel EPS Buyumesi",
                    value = "${String.format("%.1f", (fin?.realEpsGrowthPct ?: 0.0) * 100)}%",
                    explanation = "Sirketin hisse basi kari, enflasyondan arindirildiktan sonra %${String.format("%.1f", (fin?.realEpsGrowthPct ?: 0.0) * 100)} buyumus. Gercek buyumeyi temsil eder."
                )
            }
        }

        // 4. Sectoral Benchmark
        item {
            Text(text = "Sektor Kiyaslama", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            detail.sectorBenchmark?.let { bm ->
                SectorBenchmarkCard(detail.ticker, bm)
            } ?: Text("Bu sektor icin kiyaslama verisi bulunamadi.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }

        // 5. Model Detail Scores
        item {
            Text(text = "Model Skorlari", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val f = detail.factors
                    SimpleScoreRow("Buffett Kalite", f.buffett)
                    SimpleScoreRow("Graham Deger", f.graham)
                    SimpleScoreRow("Piotroski Mali", f.piotroski)
                    SimpleScoreRow("Momentum Gücü", f.momentum)
                    SimpleScoreRow("DCF Iskonto", f.dcfMos)
                }
            }
        }
    }
}

@Composable
fun InclusionReason(title: String, desc: String) {
    Column(Modifier.padding(vertical = 2.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun InsightMetricCard(
    label: String, 
    value: String, 
    explanation: String,
    benchmark: Double? = null,
    benchmarkLabel: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
                Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            }
            if (benchmark != null) {
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$benchmarkLabel: ${String.format("%.1f", benchmark * 100)}%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(text = explanation, style = androidx.compose.ui.text.TextStyle(fontSize = 11.sp, lineHeight = 15.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SectorBenchmarkCard(ticker: String, bm: SectorBenchmark) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = "${bm.sector.uppercase()} Sektoru Ortalamalari", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            BenchmarkRow("ROE (Medyan)", "${String.format("%.1f", (bm.roeMedian ?: 0.0) * 100)}%")
            BenchmarkRow("ROA (Medyan)", "${String.format("%.1f", (bm.roaMedian ?: 0.0) * 100)}%")
            BenchmarkRow("Sektor Sirket Sayisi", "${bm.companyCount ?: 0}")
        }
    }
}

@Composable
fun BenchmarkRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SimpleScoreRow(label: String, score: Double?) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(
            text = String.format("%.0f", score ?: 0.0),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = getScoreColor(score ?: 0.0)
        )
    }
}

fun getScoreColor(score: Double): Color {
    return when {
        score >= 80 -> Color(0xFF2E7D32)
        score >= 60 -> Color(0xFF4CAF50)
        score >= 40 -> Color(0xFFFFC107)
        else -> Color(0xFFD32F2F)
    }
}

@Composable
fun DetailMetric(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MetricRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
