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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bistpicker.mobile.AppContainerProvider
import com.bistpicker.mobile.data.StockDetail

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
                title = { Text(ticker, fontWeight = FontWeight.Bold) },
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
                    Text(state.message, modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun DetailContent(detail: StockDetail) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column {
                Text(detail.ticker, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                Text(detail.name ?: "", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.outline)
            }
        }

        item {
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Genel Bakis", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        DetailMetric("Alpha Skoru", String.format("%.1f", detail.alpha ?: 0.0))
                        DetailMetric("Risk Grubu", detail.risk.name)
                        DetailMetric("Sektor", detail.sector ?: "--")
                    }
                }
            }
        }

        item {
            Text("Neden Portfoyde?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val f = detail.factors
                    if ((f.buffett ?: 0.0) > 80) {
                        InclusionReason(
                            "Yuksek Kalite (Buffett)",
                            "Sirket, yuksek karliligi ve guclu rekabet avantaji sayesinde uzun vadeli 'Bileşik Getiri' makinesi ozelligi tasiyor."
                        )
                    }
                    if ((f.graham ?: 0.0) > 80) {
                        InclusionReason(
                            "Ciddi Iskonto (Graham)",
                            "Hisse fiyati, Benjamin Graham'in muhafazakar degerleme yontemine gore varliklarinin cok altinda islem goruyor."
                        )
                    }
                    if ((f.momentum ?: 0.0) > 80) {
                        InclusionReason(
                            "Guclu Trend (Momentum)",
                            "Hisse, piyasa ortalamasinin cok uzerinde bir yukselis ivmesi yakalamis durumda ve trend korundukca elde tutuluyor."
                        )
                    }
                    if ((f.piotroski ?: 0.0) >= 7) {
                        InclusionReason(
                            "Mali Iyilesme (F-Score)",
                            "Piotroski kriterlerine gore sirketin operasyonel verimliligigi ve finansal yapisi gecen yıla gore belirgin sekilde iyilesmis."
                        )
                    }
                    
                    if (detail.openPosition != null && detail.openPosition.reasons.isNotEmpty()) {
                        HorizontalDivider(Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        detail.openPosition.reasons.forEach { reason ->
                            Text("• ${reason.label}: ${String.format("%.1f", reason.value)}", style = MaterialTheme.typography.bodySmall)
                        }
                    } else if (detail.alpha != null && detail.alpha > 90) {
                         InclusionReason(
                            "ALPHA Sinyali",
                            "Sirket, tum modellerin harmanlandigi ALPHA siralamasinda su an BIST'in en iyi %5'lik diliminde yer aliyor."
                        )
                    }
                }
            }
        }

        item {
            Text("Model Detaylari", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val f = detail.factors
                FactorInfoCard(
                    title = "Buffett (Kalite)",
                    score = f.buffett,
                    description = "Sirketin rekabet avantaji (moat), yuksek ozsermaye karliligi ve tutarli nakit akisi saglama gucunu olcer."
                )
                FactorInfoCard(
                    title = "Graham (Deger)",
                    score = f.graham,
                    description = "Hissenin 'ucuz' olup olmadigini, varliklarina ve reel faize gore potansiyelini Benjamin Graham formuluyle hesaplar."
                )
                FactorInfoCard(
                    title = "Piotroski (Mali Saglik)",
                    score = f.piotroski,
                    description = "9 farkli mali kriter uzerinden sirketin operasyonel verimliliginin gecen yıla gore iyilesip iyilesmedigine bakar."
                )
                FactorInfoCard(
                    title = "Momentum (Trend)",
                    score = f.momentum,
                    description = "Hisse fiyatindaki yukselis trendinin gucunu ve piyasa ortalamasina gore ne kadar hizli hareket ettigini gosterir."
                )
            }
        }

        item {
            Text("Finansal Veriler", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val fin = detail.financials
                    MetricRow("ROE (Ozkaynak Karliligi)", "${String.format("%.1f", (fin?.roeAdjusted ?: 0.0) * 100)}%")
                    MetricRow("ROA (Varlik Karliligi)", "${String.format("%.1f", (fin?.roaAdjusted ?: 0.0) * 100)}%")
                    MetricRow("EPS Buyumesi (Reel)", "${String.format("%.1f", (fin?.realEpsGrowthPct ?: 0.0) * 100)}%")
                    val fcfVal = (fin?.freeCashFlow ?: 0.0) / 1_000_000.0
                    MetricRow("Nakit Akisi (Serbest)", "${String.format("%.0f", fcfVal)} M TL")
                }
            }
        }
    }
}

@Composable
fun InclusionReason(title: String, desc: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraSmall))
            Spacer(Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        }
        Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun FactorInfoCard(title: String, score: Double?, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Surface(
                    color = getScoreColor(score ?: 0.0),
                    shape = MaterialTheme.shapes.small
                ) {
                    val sVal = score ?: 0.0
                    Text(
                        text = String.format("%.0f", sVal),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }
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
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MetricRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
