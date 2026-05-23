package com.bistpicker.mobile.ui.screens.macro

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bistpicker.mobile.AppContainerProvider
import com.bistpicker.mobile.data.HomeData
import com.bistpicker.mobile.data.SnapshotInfo
import com.bistpicker.mobile.data.CashState
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
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
    var animateEntrance by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animateEntrance = true
    }

    AnimatedVisibility(
        visible = animateEntrance,
        enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
            initialOffsetY = { 60 },
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        "Piyasa ve Strateji",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "Makroekonomik veriler ve sinyal durumları",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                RealYieldCard(data)
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
}

@Composable
fun RealYieldCard(data: HomeData) {
    val macro = data.macro ?: return
    val policyRate = macro.policyRatePct ?: 0.0
    val inflation = macro.cpiYoyPct ?: 0.0
    val realYield = policyRate - inflation
    val isPositive = realYield > 0

    val yieldColor = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444)
    val yieldText = if (isPositive) "+${String.format("%.2f", realYield * 100)}%" else "${String.format("%.2f", realYield * 100)}%"

    var animateTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animateTrigger = true
    }
    val targetBias = (realYield.coerceIn(-0.15, 0.15) / 0.30).toFloat() * 2f
    val animatedBias by animateFloatAsState(
        targetValue = if (animateTrigger) targetBias else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        ),
        border = BorderStroke(
            1.dp,
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Reel Faiz Getirisi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Politika Faizi - Yıllık Enflasyon",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(yieldColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .border(1.dp, yieldColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Speed,
                        contentDescription = null,
                        tint = yieldColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = yieldText,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = yieldColor
                )
                
                Surface(
                    color = yieldColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, yieldColor.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = if (isPositive) "POZİTİF REEL GETİRİ" else "NEGATİF REEL GETİRİ",
                        style = MaterialTheme.typography.labelSmall,
                        color = yieldColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Custom slider gauge
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFEF4444).copy(alpha = 0.9f),
                                    Color(0xFFF59E0B).copy(alpha = 0.7f),
                                    Color(0xFF10B981).copy(alpha = 0.9f)
                                )
                            )
                        )
                ) {
                    // Center 0% divider
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(Color.White.copy(alpha = 0.8f))
                            .align(Alignment.Center)
                    )
                    
                    // Thumb pin
                    Box(
                        modifier = Modifier
                            .align(BiasAlignment(animatedBias, 0f))
                            .size(16.dp)
                            .shadow(3.dp, RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .border(2.5.dp, yieldColor, RoundedCornerShape(8.dp))
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("-15%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("0% Dengede", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
                    Text("+15%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Strategy Tips
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = if (isPositive) Icons.Rounded.CheckCircle else Icons.Rounded.Warning,
                        contentDescription = null,
                        tint = yieldColor,
                        modifier = Modifier.size(20.dp).offset(y = 2.dp)
                    )
                    Text(
                        text = if (isPositive) {
                            "Pozitif Reel Faiz: Nakit ve risksiz mevduat getirileri enflasyon karşısında değerini korur. Borsa İstanbul'da seçici olunmalı, çarpanları ucuz ve yüksek büyüme kapasiteli ALPHA Core şirketleri tercih edilmelidir."
                        } else {
                            "Negatif Reel Faiz: Nakit ve mevduat tutmak reel bazda kayıp yaratır. Hisse senetleri ve reel varlıklar enflasyona karşı doğal koruma kalkanıdır. BIST100 endeksi genel olarak desteklenir."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PerformanceSummary(data: HomeData) {
    val perf = data.performance ?: return
    
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        ),
        border = BorderStroke(
            1.dp,
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Portföy Performansı",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Model portföyün genel başarı oranları",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ShowChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val totalReturn = perf.totalReturnAvg ?: 0.0
                val benchmark = perf.benchmarkYtd ?: 0.0
                val winRate = perf.winRate ?: 0.0
                
                PerformanceItem(
                    label = "Ort. Getiri",
                    value = "${if (totalReturn >= 0) "+" else ""}${String.format("%.1f", totalReturn * 100)}%",
                    color = if (totalReturn >= 0) Color(0xFF10B981) else Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )
                
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        .align(Alignment.CenterVertically)
                )
                
                PerformanceItem(
                    label = "BIST100 YTD",
                    value = "${if (benchmark >= 0) "+" else ""}${String.format("%.1f", benchmark * 100)}%",
                    color = if (benchmark >= 0) Color(0xFF10B981) else Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )
                
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        .align(Alignment.CenterVertically)
                )
                
                PerformanceItem(
                    label = "Kazanma Oranı",
                    value = "${String.format("%.0f", winRate)}%",
                    color = if (winRate >= 50) Color(0xFF10B981) else Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PerformanceItem(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}

@Composable
fun CashStrategyCard(data: HomeData) {
    val cash = data.cash ?: return
    
    val (stateColor, stateDesc, bgGradient) = when (cash.state) {
        CashState.NORMAL -> Triple(
            Color(0xFF10B981), 
            "NORMAL (Hisse Ağırlıklı)",
            Brush.verticalGradient(listOf(Color(0xFF10B981).copy(alpha = 0.08f), Color.Transparent))
        )
        CashState.CAUTION -> Triple(
            Color(0xFFF59E0B), 
            "İHTİYAT (Seçici Nakit)",
            Brush.verticalGradient(listOf(Color(0xFFF59E0B).copy(alpha = 0.08f), Color.Transparent))
        )
        CashState.DEFENSIVE -> Triple(
            Color(0xFFD97706), 
            "SAVUNMA (Yüksek Nakit)",
            Brush.verticalGradient(listOf(Color(0xFFD97706).copy(alpha = 0.08f), Color.Transparent))
        )
        CashState.RISK_OFF -> Triple(
            Color(0xFFEF4444), 
            "RİSK DIŞI (Tam Nakit)",
            Brush.verticalGradient(listOf(Color(0xFFEF4444).copy(alpha = 0.08f), Color.Transparent))
        )
        else -> Triple(
            MaterialTheme.colorScheme.onSurfaceVariant, 
            cash.state.name,
            Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        ),
        border = BorderStroke(
            1.dp,
            Brush.verticalGradient(
                colors = listOf(
                    stateColor.copy(alpha = 0.4f),
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .background(bgGradient)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Nakit ve Risk Yönetimi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Sinyal Mekanizması (Phase 4)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(stateColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .border(1.dp, stateColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Wallet,
                        contentDescription = null,
                        tint = stateColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "Mevcut Strateji Durumu",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = stateColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, stateColor.copy(alpha = 0.25f))
                    ) {
                        Text(
                            text = stateDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = stateColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "Hedef Sinyal",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = cash.targetState.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Portföy Dağılımı",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${String.format("%.0f", (cash.cashPct ?: 0.0) * 100)}% Nakit / ${String.format("%.0f", (1.0 - (cash.cashPct ?: 0.0)) * 100)}% Hisse",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                val cashPct = (cash.cashPct ?: 0.0).toFloat()
                LinearProgressIndicator(
                    progress = { cashPct },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = stateColor,
                    trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            }
            
            if (!cash.notes.isNullOrBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = cash.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MacroStatsCard(data: HomeData) {
    val macro = data.macro ?: return
    
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        ),
        border = BorderStroke(
            1.dp,
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Ekonomik Göstergeler",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "En son güncellenen piyasa verileri",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccountBalance,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val regimeColor = if (macro.regime == "RISK_ON") Color(0xFF10B981) else Color(0xFFEF4444)
                    
                    GridItem(
                        icon = Icons.AutoMirrored.Rounded.TrendingUp,
                        iconTint = regimeColor,
                        label = "Piyasa Rejimi",
                        value = macro.regime ?: "--",
                        valueColor = regimeColor,
                        modifier = Modifier.weight(1f)
                    )
                    
                    GridItem(
                        icon = Icons.Rounded.Percent,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        label = "TCMB Politika Faizi",
                        value = "${String.format("%.1f", (macro.policyRatePct ?: 0.0) * 100)}%",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GridItem(
                        icon = Icons.AutoMirrored.Rounded.TrendingUp,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        label = "Yıllık Enflasyon (YoY)",
                        value = "${String.format("%.1f", (macro.cpiYoyPct ?: 0.0) * 100)}%",
                        modifier = Modifier.weight(1f)
                    )
                    
                    GridItem(
                        icon = Icons.Rounded.MonetizationOn,
                        iconTint = Color(0xFF10B981),
                        label = "Dolar Kuru",
                        value = "${String.format("%.2f", macro.usdTryRate ?: 0.0)} TL",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun GridItem(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(iconTint.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = valueColor
            )
        }
    }
}

@Composable
fun SyncStatusCard(info: SnapshotInfo?, sync: SyncState?) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Sistem ve Veri Durumu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SyncDataRow("Senkronizasyon Durumu", sync?.phase?.name ?: "IDLE")
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)))
                SyncDataRow("Snapshot Veri Tarihi", info?.snapshotDate ?: "--")
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)))
                SyncDataRow("Modeldeki Şirket Sayısı", "${info?.companyCount ?: 0}")
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)))
                SyncDataRow("Veri Şema Versiyonu", "v${info?.schemaVersion ?: 0}")
            }
        }
    }
}

@Composable
fun SyncDataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
