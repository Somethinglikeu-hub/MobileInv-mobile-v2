package com.bistpicker.mobile.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.bistpicker.mobile.data.ClosedPosition
import com.bistpicker.mobile.data.ModelPerformancePoint

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(
            repository = AppContainerProvider.get(LocalContext.current).repository
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        Box(Modifier.padding(innerPadding).fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
            when (val state = uiState) {
                is HistoryUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is HistoryUiState.Success -> {
                    HistoryContent(state.performance, state.closedPositions)
                }
            }
        }
    }
}

@Composable
fun HistoryContent(performance: List<ModelPerformancePoint>, closed: List<ClosedPosition>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Model Performansi (1 Yil)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
            ModelPerformanceCard(performance)
        }

        item {
            Text("Gecmis Islemler", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
        }

        if (closed.isEmpty()) {
            item {
                Text("Henüz tamamlanmis islem bulunmuyor.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
        } else {
            items(closed) { pos ->
                ClosedPositionCard(pos)
            }
        }
    }
}

@Composable
fun ModelPerformanceCard(performance: List<ModelPerformancePoint>) {
    val last = performance.lastOrNull()
    val first = performance.firstOrNull()
    
    val totalReturn = if (last != null) last.strategyReturn - 100.0 else 0.0
    val benchReturn = if (last != null) last.benchmarkReturn - 100.0 else 0.0
    
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                PerformanceMetric("Model Getirisi", "${String.format("%.1f", totalReturn)}%", Color(0xFF4CAF50))
                PerformanceMetric("BIST100 (Proxy)", "${String.format("%.1f", (last?.benchmarkReturn ?: 100.0) - 100.0)}%", MaterialTheme.colorScheme.outline)
            }
            
            Spacer(Modifier.height(16.dp))
            
            val alpha = (last?.strategyReturn ?: 0.0) - (last?.benchmarkReturn ?: 0.0)
            Surface(
                color = if (alpha >= 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Model Endeksi ${String.format("%.1f", alpha)}% yendi.",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (alpha >= 0) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )
            }
            
            Spacer(Modifier.height(8.dp))
            Text(
                "Not: Backtest 'Point-in-Time' (Geriye Dönük Veri İzolasyonu) kurallarına göre, her tarihte sadece o an yayinlanmis bilançolar kullanilarak hesaplanmistir.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun PerformanceMetric(label: String, value: String, color: Color) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = color)
    }
}

@Composable
fun ClosedPositionCard(pos: ClosedPosition) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(pos.ticker, fontWeight = FontWeight.Bold)
                Text("${pos.selectionDate} - ${pos.exitDate}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            Column(horizontalAlignment = Alignment.End) {
                val pnl = pos.pnlPct ?: 0.0
                Text(
                    "${if (pnl >= 0) "+" else ""}${String.format("%.1f", pnl)}%",
                    fontWeight = FontWeight.Black,
                    color = if (pnl >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                Text(pos.exitReason ?: "", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}
