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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bistpicker.mobile.AppContainerProvider
import com.bistpicker.mobile.data.ClosedPosition
import com.bistpicker.mobile.data.HomeData
import com.bistpicker.mobile.ui.screens.home.HomeUiState
import com.bistpicker.mobile.ui.screens.home.HomeViewModel

@Composable
fun HistoryScreen(
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
                    HistoryContent(state.data)
                }
            }
        }
    }
}

@Composable
fun HistoryContent(data: HomeData) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Son Islemler", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            Text("Satis yapilan hisselerin gecmisi", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.height(8.dp))
        }

        if (data.history.isEmpty()) {
            item {
                Box(Modifier.fillParentMaxHeight(0.7f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Henuz islem gecmisi bulunmuyor.", color = MaterialTheme.colorScheme.outline)
                }
            }
        } else {
            items(data.history) { closed ->
                HistoryItemCard(closed)
            }
        }
    }
}

@Composable
fun HistoryItemCard(closed: ClosedPosition) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(closed.ticker, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Cikis: ${closed.exitDate ?: "--"}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Text(
                    text = closed.exitReason ?: "REBALANCE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${closed.exitPrice ?: 0.0} TL", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                val pnl = (closed.pnlPct ?: 0.0)
                Text(
                    "${if (pnl >= 0) "+" else ""}${String.format("%.2f", pnl)}%",
                    color = if (pnl >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
