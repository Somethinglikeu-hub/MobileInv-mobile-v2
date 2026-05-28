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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bistpicker.mobile.AppContainerProvider
import com.bistpicker.mobile.data.ClosedPosition
import com.bistpicker.mobile.data.ModelPerformancePoint
import com.bistpicker.mobile.data.OpenPosition
import com.bistpicker.mobile.data.WeeklyPerformanceRecord
import com.bistpicker.mobile.data.WeeklyStockRecord
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryScreen(
    onNavigateToBacktesting: () -> Unit,
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
                    HistoryContent(
                        closed = state.closedPositions,
                        openPositions = state.openPositions,
                        weeklyPerformance = state.weeklyPerformance,
                        onNavigateToBacktesting = onNavigateToBacktesting
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryContent(
    closed: List<ClosedPosition>,
    openPositions: List<OpenPosition>,
    weeklyPerformance: List<WeeklyPerformanceRecord>,
    onNavigateToBacktesting: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BacktestingNavigationCard(onNavigateToBacktesting)
        }

        item {
            Text("Reel Portföy Performansı", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
            LivePortfolioPerformanceCard(weeklyPerformance)
        }

        item {
            Text("Haftalık Performans Geçmişi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        }

        if (weeklyPerformance.isEmpty()) {
            item {
                Text("Henüz haftalık takip verisi bulunmuyor.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
        } else {
            items(weeklyPerformance.reversed()) { record ->
                WeeklyPerformanceRecordCard(record)
            }
        }
    }
}

@Composable
fun BacktestingNavigationCard(onNavigateToBacktesting: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onNavigateToBacktesting,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFE91E63), // Pink
                            Color(0xFF5E3DBF)  // Deep Purple / Tertiary
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Model Backtesting (10 Yıl)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Point-in-Time simülasyonu ile 10 yıllık model performans grafiği ve detayları için tıklayın.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 15.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Detaylar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun LivePortfolioPerformanceCard(
    weeklyPerformance: List<WeeklyPerformanceRecord>
) {
    if (weeklyPerformance.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Box(Modifier.padding(16.dp)) {
                Text("Kümülatif hesaplama için haftalık veri bekleniyor.")
            }
        }
        return
    }

    // Calculate cumulative returns
    var cumulativePortfolio = 1.0
    var cumulativeBist100 = 1.0
    weeklyPerformance.forEach { record ->
        cumulativePortfolio *= (1.0 + record.portfolioReturn)
        cumulativeBist100 *= (1.0 + record.bist100Return)
    }
    
    val totalPortfolioReturn = (cumulativePortfolio - 1.0) * 100.0
    val totalBist100Return = (cumulativeBist100 - 1.0) * 100.0
    val totalAlpha = totalPortfolioReturn - totalBist100Return

    val hasActiveWeek = weeklyPerformance.any { !it.isCompleted }
    val startDate = weeklyPerformance.firstOrNull()?.weekStartDate ?: "2026-05-18"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Kümülatif Portföy Getirisi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Başlangıç: ${formatDateToTurkish(startDate)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                
                if (hasActiveWeek) {
                    Surface(
                        color = Color(0xFFE91E63),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            text = "CANLI TAKİP",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                PerformanceMetric(
                    label = "Toplam Getiri",
                    value = "${if (totalPortfolioReturn >= 0) "+" else ""}${String.format("%.2f", totalPortfolioReturn)}%",
                    color = if (totalPortfolioReturn >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                PerformanceMetric(
                    label = "BIST100 Endeksi",
                    value = "${if (totalBist100Return >= 0) "+" else ""}${String.format("%.2f", totalBist100Return)}%",
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            Surface(
                color = if (totalAlpha >= 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Endekse Karşı Fark: ${if (totalAlpha >= 0) "+" else ""}${String.format("%.2f", totalAlpha)}%",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (totalAlpha >= 0) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )
            }
        }
    }
}

@Composable
fun WeeklyPerformanceRecordCard(record: WeeklyPerformanceRecord) {
    var expanded by remember { mutableStateOf(!record.isCompleted) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder(),
        onClick = { expanded = !expanded }
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${formatDateToTurkish(record.weekStartDate)} Haftası",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    val diff = (record.portfolioReturn - record.bist100Return) * 100.0
                    Text(
                        text = "Haftalık Fark: ${if (diff >= 0) "+" else ""}${String.format("%.2f", diff)}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (diff >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!record.isCompleted) {
                        Surface(
                            color = Color(0xFFE91E63).copy(alpha = 0.1f),
                            shape = MaterialTheme.shapes.extraSmall,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE91E63))
                        ) {
                            Text(
                                text = "AKTİF",
                                color = Color(0xFFE91E63),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Daralt" else "Genişlet",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Haftalık Portföy", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "${if (record.portfolioReturn >= 0) "+" else ""}${String.format("%.2f", record.portfolioReturn * 100.0)}%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Black,
                        color = if (record.portfolioReturn >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("BIST100 Getirisi", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "${if (record.bist100Return >= 0) "+" else ""}${String.format("%.2f", record.bist100Return * 100.0)}%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            if (expanded) {
                Spacer(Modifier.height(16.dp))
                Spacer(Modifier.height(1.dp).fillMaxWidth().background(MaterialTheme.colorScheme.outlineVariant))
                Spacer(Modifier.height(12.dp))
                
                Text(
                    text = "Seçilen Hisseler ve Performansları",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                
                record.positions.forEach { stock ->
                    val pnl = stock.returnPct * 100.0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stock.ticker,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${String.format("%.2f", stock.entryPrice)} → ${String.format("%.2f", stock.exitPrice)} TL",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "${if (pnl >= 0) "+" else ""}${String.format("%.2f", pnl)}%",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (pnl >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                        }
                    }
                }
            }
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

fun formatDateToTurkish(dateStr: String): String {
    return try {
        val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = sdfInput.parse(dateStr) ?: return dateStr
        val sdfOutput = SimpleDateFormat("d MMMM yyyy", Locale("tr", "TR"))
        sdfOutput.format(date)
    } catch (e: Exception) {
        dateStr
    }
}
