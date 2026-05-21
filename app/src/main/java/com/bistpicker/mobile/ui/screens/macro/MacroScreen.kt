package com.bistpicker.mobile.ui.screens.macro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.bistpicker.mobile.data.HomeData
import com.bistpicker.mobile.data.SnapshotInfo
import com.bistpicker.mobile.data.sync.SyncState
import com.bistpicker.mobile.ui.screens.home.HomeUiState
import com.bistpicker.mobile.ui.screens.home.HomeViewModel

@Composable
fun MacroScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(
            repository = AppContainerProvider.get(LocalContext.current).repository,
            syncStore = AppContainerProvider.get(LocalContext.current).syncState
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        Box(Modifier.padding(innerPadding).fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is HomeUiState.Success -> {
                    MacroContent(state.data, state.info, state.sync)
                }
            }
        }
    }
}

@Composable
fun MacroContent(data: HomeData, info: SnapshotInfo?, sync: SyncState?) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text("Piyasa ve Strateji", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        }

        item {
            PerformanceSummary(data)
        }

        item {
            CashStrategyCard(data)
        }

        item {
            MacroStatsCard(data)
        }

        item {
            SyncStatusCard(info, sync)
        }
    }
}

@Composable
fun PerformanceSummary(data: HomeData) {
    val perf = data.performance ?: return
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Portfoy Performansi", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MacroStatItem("Toplam Kar", "${String.format("%.1f", (perf.totalReturnAvg ?: 0.0) * 100)}%")
                MacroStatItem("Benchmark", "${String.format("%.1f", (perf.benchmarkYtd ?: 0.0) * 100)}%")
                MacroStatItem("Win Rate", "${String.format("%.0f", perf.winRate ?: 0.0)}%")
            }
        }
    }
}

@Composable
fun CashStrategyCard(data: HomeData) {
    val cash = data.cash ?: return
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Nakit Yonetimi (Phase 4)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MacroStatItem("Mevcut", cash.state.name)
                MacroStatItem("Hedef", cash.targetState.name)
                MacroStatItem("Nakit %", "${String.format("%.0f", (cash.cashPct ?: 0.0) * 100)}%")
            }
            if (!cash.notes.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = cash.notes,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MacroStatsCard(data: HomeData) {
    val macro = data.macro ?: return
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Ekonomik Gostergeler", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            MacroDataRow("Piyasa Rejimi", macro.regime ?: "--")
            MacroDataRow("TCMB Faiz", "${String.format("%.1f", (macro.policyRatePct ?: 0.0) * 100)}%")
            MacroDataRow("Enflasyon (YoY)", "${String.format("%.1f", (macro.cpiYoyPct ?: 0.0) * 100)}%")
            MacroDataRow("Dolar Kuru", "${String.format("%.2f", macro.usdTryRate ?: 0.0)} TL")
        }
    }
}

@Composable
fun SyncStatusCard(info: SnapshotInfo?, sync: SyncState?) {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Sistem ve Veri Durumu", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            MacroDataRow("Senkronizasyon", sync?.phase?.name ?: "IDLE")
            MacroDataRow("Veri Tarihi", info?.snapshotDate ?: "--")
            MacroDataRow("Sirket Sayisi", "${info?.companyCount ?: 0}")
            MacroDataRow("Sema Versiyonu", "v${info?.schemaVersion ?: 0}")
        }
    }
}

@Composable
fun MacroStatItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MacroDataRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
