package com.bistpicker.mobile.ui.screens.backtesting

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bistpicker.mobile.AppContainerProvider
import com.bistpicker.mobile.data.ModelPerformancePoint
import com.bistpicker.mobile.ui.screens.history.HistoryUiState
import com.bistpicker.mobile.ui.screens.history.HistoryViewModel
import com.bistpicker.mobile.ui.screens.history.PerformanceMetric
import com.bistpicker.mobile.data.ClosedPosition
import com.bistpicker.mobile.ui.screens.history.ClosedPositionCard

@Composable
fun BacktestingScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(
            repository = AppContainerProvider.get(LocalContext.current).repository
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Geri",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Backtesting (1 Yil)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            when (val state = uiState) {
                is HistoryUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is HistoryUiState.Success -> {
                    BacktestingContent(
                        performance = state.performance,
                        closed = state.closedPositions
                    )
                }
            }
        }
    }
}

@Composable
fun BacktestingContent(
    performance: List<ModelPerformancePoint>,
    closed: List<ClosedPosition>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ModelPerformanceDetailCard(performance)
        }

        if (performance.isNotEmpty()) {
            item {
                Text(
                    text = "Performans Grafiği (Kümplatif %)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                ModelPerformanceChart(performance)
            }
        }

        item {
            Text(
                "Aylık/Haftalık Geçmiş Model Puan Değişimleri",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
        }

        if (performance.isEmpty()) {
            item {
                Text(
                    "Geçmiş veri bulunmuyor.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            items(performance.reversed()) { point ->
                PerformancePointRow(point)
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            Text(
                "Geçmiş Model İşlemleri",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
        }

        if (closed.isEmpty()) {
            item {
                Text(
                    "Henüz tamamlanmış model işlemi bulunmuyor.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            items(closed) { pos ->
                ClosedPositionCard(pos)
            }
        }
    }
}

@Composable
fun ModelPerformanceDetailCard(performance: List<ModelPerformancePoint>) {
    val last = performance.lastOrNull()
    
    val totalReturn = if (last != null) last.strategyReturn - 100.0 else 0.0
    val benchReturn = if (last != null) last.benchmarkReturn - 100.0 else 0.0
    val alpha = (last?.strategyReturn ?: 100.0) - (last?.benchmarkReturn ?: 100.0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.05f)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                PerformanceMetric(
                    label = "Model Toplam Getiri",
                    value = "%${String.format("%.1f", totalReturn)}",
                    color = if (totalReturn >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                PerformanceMetric(
                    label = "BIST100 (Proxy)",
                    value = "%${String.format("%.1f", benchReturn)}",
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            Surface(
                color = if (alpha >= 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Alpha (Endeks Üstü Getiri): ${if (alpha >= 0) "+" else ""}${String.format("%.1f", alpha)}%",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (alpha >= 0) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )
            }
            
            Spacer(Modifier.height(12.dp))
            Text(
                "Bu simülasyon, her Pazartesi sabahı o anki en güncel bilançolarla yeniden oluşturulan point-in-time portföylerin getirilerini kümülatif olarak yansıtır. Geleceğe bakma hatası (look-ahead bias) barındırmaz.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun ModelPerformanceChart(performance: List<ModelPerformancePoint>) {
    val modelPoints = performance.map { it.strategyReturn }
    val benchPoints = performance.map { it.benchmarkReturn }
    
    val maxVal = maxOf(
        modelPoints.maxOrNull() ?: 100.0,
        benchPoints.maxOrNull() ?: 100.0,
        100.0
    )
    val minVal = minOf(
        modelPoints.minOrNull() ?: 100.0,
        benchPoints.minOrNull() ?: 100.0,
        100.0
    )
    
    val range = (maxVal - minVal).coerceAtLeast(1.0)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val stepX = width / (performance.size - 1).coerceAtLeast(1)

            val modelPath = Path()
            val benchPath = Path()

            performance.forEachIndexed { index, point ->
                val x = index * stepX
                val modelY = height - ((point.strategyReturn - minVal) / range * height).toFloat()
                val benchY = height - ((point.benchmarkReturn - minVal) / range * height).toFloat()

                if (index == 0) {
                    modelPath.moveTo(x, modelY)
                    benchPath.moveTo(x, benchY)
                } else {
                    modelPath.lineTo(x, modelY)
                    benchPath.lineTo(x, benchY)
                }
            }

            drawPath(
                path = modelPath,
                color = Color(0xFF4CAF50),
                style = Stroke(width = 3.dp.toPx())
            )

            drawPath(
                path = benchPath,
                color = Color(0xFF9E9E9E),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
    
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).background(Color(0xFF4CAF50), shape = MaterialTheme.shapes.extraSmall))
            Spacer(Modifier.width(4.dp))
            Text("Model", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).background(Color(0xFF9E9E9E), shape = MaterialTheme.shapes.extraSmall))
            Spacer(Modifier.width(4.dp))
            Text("BIST100 Proxy", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun PerformancePointRow(point: ModelPerformancePoint) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = point.date,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Alpha: ${if (point.alpha >= 0) "+" else ""}${String.format("%.2f", point.alpha)}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (point.alpha >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("Model", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(
                        "%${String.format("%.1f", point.strategyReturn - 100.0)}",
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4CAF50)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("BIST100", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(
                        "%${String.format("%.1f", point.benchmarkReturn - 100.0)}",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
