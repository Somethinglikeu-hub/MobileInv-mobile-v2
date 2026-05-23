package com.bistpicker.mobile.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.CheckCircle
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
import com.bistpicker.mobile.data.*
import com.bistpicker.mobile.data.sync.SyncState
import com.bistpicker.mobile.data.sync.SnapshotSyncWorker
import com.bistpicker.mobile.data.sync.SyncPhase
import androidx.compose.animation.AnimatedVisibility

@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(
            repository = AppContainerProvider.get(LocalContext.current).repository,
            syncStore = AppContainerProvider.get(LocalContext.current).syncState
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Box(Modifier.padding(innerPadding).fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is HomeUiState.Success -> {
                    val isEmpty = state.data.openPositions.isEmpty() && state.data.suggestions.isEmpty() && state.info == null
                    val syncPhase = state.sync?.phase ?: SyncPhase.Idle
                    val isSyncing = syncPhase in listOf(SyncPhase.Fetching, SyncPhase.Extracting, SyncPhase.Applying)

                    if (isEmpty) {
                        EmptyOrLoadingScreen(
                            syncPhase = syncPhase,
                            isSyncing = isSyncing,
                            onRefresh = {
                                viewModel.refresh()
                                SnapshotSyncWorker.enqueueOneOff(context)
                            }
                        )
                    } else {
                        HomeContent(
                            data = state.data,
                            syncState = state.sync,
                            onNavigateToDetail = onNavigateToDetail,
                            onRefresh = {
                                viewModel.refresh()
                                SnapshotSyncWorker.enqueueOneOff(context)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyOrLoadingScreen(
    syncPhase: SyncPhase,
    isSyncing: Boolean,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.TrendingUp,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "BIST Picker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        
        if (isSyncing) {
            val statusMessage = when (syncPhase) {
                SyncPhase.Fetching -> "Güncel model verileri indiriliyor..."
                SyncPhase.Extracting -> "Veri paketi açılıyor..."
                SyncPhase.Applying -> "Veritabanı yapılandırılıyor..."
                else -> "Lütfen bekleyin..."
            }
            Spacer(Modifier.height(24.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Uygulamayı kullanmaya başlamak için güncel verilerin indirilmesi gerekiyor.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onRefresh,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Verileri Şimdi İndir")
            }
        }
    }
}

@Composable
fun HomeContent(
    data: HomeData,
    syncState: SyncState?,
    onNavigateToDetail: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val syncPhase = syncState?.phase ?: SyncPhase.Idle
    val isSyncing = syncPhase in listOf(SyncPhase.Fetching, SyncPhase.Extracting, SyncPhase.Applying)

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = isSyncing) {
            val statusMessage = when (syncPhase) {
                SyncPhase.Fetching -> "Güncel veriler indiriliyor..."
                SyncPhase.Extracting -> "Veri paketi açılıyor..."
                SyncPhase.Applying -> "Veritabanı güncelleniyor..."
                else -> ""
            }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = statusMessage,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                HeaderSection(
                    weekStart = data.weekStart,
                    weekEnd = data.weekEnd,
                    isSyncing = isSyncing,
                    onRefresh = onRefresh
                )
            }

            if (data.suggestions.isNotEmpty()) {
                item {
                    SuggestionsSection(data.suggestions, onNavigateToDetail)
                }
            }

            item {
                Text(
                    "Aktif Portfoy",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (data.openPositions.isEmpty()) {
                item {
                    Text("Portfoyde hisse bulunmuyor.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                }
            } else {
                items(data.openPositions, key = { it.ticker }) { position ->
                    PositionCardSmall(position, onClick = { onNavigateToDetail(position.ticker) })
                }
            }
        
        item {
            Spacer(Modifier.height(16.dp))
        }
    }
}
}

@Composable
fun HeaderSection(weekStart: String?, weekEnd: String?, isSyncing: Boolean, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Picks", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            val weekText = if (weekStart != null) "$weekStart - $weekEnd" else "Portfoy Yonetimi"
            Text(weekText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
        if (isSyncing) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            IconButton(onClick = onRefresh, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun SuggestionsSection(suggestions: List<SuggestedAction>, onNavigateToDetail: (String) -> Unit) {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder(enabled = true)
    ) {
        Column(Modifier.padding(10.dp)) {
            Text("Zeki Sinyaller", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
            Spacer(Modifier.height(4.dp))
            suggestions.forEach { suggestion ->
                SuggestionRowSmall(suggestion) { onNavigateToDetail(suggestion.ticker) }
            }
        }
    }
}

@Composable
fun SuggestionRowSmall(suggestion: SuggestedAction, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                SuggestionIconSmall(suggestion.action)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(suggestion.ticker, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Text(suggestion.reason ?: "", style = androidx.compose.ui.text.TextStyle(fontSize = 10.sp), color = MaterialTheme.colorScheme.outline)
                }
            }
            Text(
                when(suggestion.action) {
                    TradeAction.BUY -> "AL"
                    TradeAction.SELL -> "SAT"
                    TradeAction.HOLD -> "TUT"
                },
                fontWeight = FontWeight.Black,
                color = getActionColor(suggestion.action),
                style = androidx.compose.ui.text.TextStyle(fontSize = 11.sp)
            )
        }
    }
}

@Composable
fun SuggestionIconSmall(action: TradeAction) {
    val (icon, color) = when (action) {
        TradeAction.BUY -> Icons.Default.TrendingUp to Color(0xFF4CAF50)
        TradeAction.SELL -> Icons.Default.TrendingDown to Color(0xFFF44336)
        TradeAction.HOLD -> Icons.Default.CheckCircle to Color(0xFF2196F3)
    }
    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
}

fun getActionColor(action: TradeAction): Color = when (action) {
    TradeAction.BUY -> Color(0xFF2E7D32)
    TradeAction.SELL -> Color(0xFFD32F2F)
    TradeAction.HOLD -> Color(0xFF1976D2)
}

@Composable
fun PositionCardSmall(position: OpenPosition, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(position.ticker, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                    if (position.isLive) {
                        Spacer(Modifier.width(8.dp))
                        Surface(color = Color(0xFFE91E63), shape = MaterialTheme.shapes.extraSmall) {
                            Text("CANLI", modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp), color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 8.sp, fontWeight = FontWeight.Black))
                        }
                    }
                }
                Text(position.name ?: "", style = androidx.compose.ui.text.TextStyle(fontSize = 10.sp), color = MaterialTheme.colorScheme.outline, maxLines = 1)
            }
            Column(horizontalAlignment = Alignment.End) {
                if (position.isLive) {
                    Text("Snap: ${position.snapshotPrice ?: 0.0}", style = androidx.compose.ui.text.TextStyle(fontSize = 9.sp), color = MaterialTheme.colorScheme.outline)
                    Text("Canlı: ${position.currentPrice ?: 0.0}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                } else {
                    Text("${position.currentPrice ?: 0.0} TL", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                val pnl = position.pnlPct ?: 0.0
                Text(
                    "${if (pnl >= 0) "+" else ""}${String.format("%.1f", pnl)}%",
                    color = if (pnl >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = androidx.compose.ui.text.TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Black)
                )
            }
        }
    }
}
