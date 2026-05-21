package com.bistpicker.mobile.ui.screens.scoring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bistpicker.mobile.AppContainerProvider
import com.bistpicker.mobile.data.ScoringPage
import com.bistpicker.mobile.data.ScoringRow
import com.bistpicker.mobile.data.ScoringViewMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoringScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: ScoringViewModel = viewModel(
        factory = ScoringViewModel.Factory(AppContainerProvider.get(LocalContext.current).repository)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val filters by viewModel.filters.collectAsState()

    Scaffold(
        topBar = {
            Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                Text(
                    "Sirket Tarama",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                
                OutlinedTextField(
                    value = filters.search ?: "",
                    onValueChange = { viewModel.setSearch(it) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    placeholder = { Text("Hisse ara...", style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(8.dp))

                ScrollableTabRow(
                    selectedTabIndex = filters.mode.ordinal,
                    edgePadding = 16.dp,
                    divider = {},
                    containerColor = Color.Transparent,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[filters.mode.ordinal]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    ScoringViewMode.entries.forEach { mode ->
                        Tab(
                            selected = filters.mode == mode,
                            onClick = { viewModel.setViewMode(mode) },
                            text = { 
                                Text(
                                    when(mode) {
                                        ScoringViewMode.ALPHA_CORE -> "ALPHA Core"
                                        ScoringViewMode.ALPHA_X -> "ALPHA X"
                                        ScoringViewMode.RESEARCH -> "Arastirma"
                                        ScoringViewMode.MODEL -> "Model"
                                        ScoringViewMode.ALL -> "Hepsi"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (filters.mode == mode) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding).fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
            when (val state = uiState) {
                is ScoringUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is ScoringUiState.Success -> {
                    ScoringList(
                        page = state.page,
                        onNavigateToDetail = onNavigateToDetail
                    )
                }
            }
        }
    }
}

@Composable
fun ScoringList(
    page: ScoringPage,
    onNavigateToDetail: (String) -> Unit
) {
    if (page.rows.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Sonuc bulunamadi.", color = MaterialTheme.colorScheme.outline)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(page.rows) { row ->
                ScoringCardSmall(row, onClick = { onNavigateToDetail(row.ticker) })
            }
        }
    }
}

@Composable
fun ScoringCardSmall(row: ScoringRow, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(row.ticker, fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyLarge)
                    if (row.isBist100) {
                        Spacer(Modifier.width(4.dp))
                        Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.extraSmall) {
                            Text("B100", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                Text(row.sector ?: "--", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("%.1f", row.alpha ?: 0.0),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = getAlphaColor(row.alpha ?: 0.0)
                )
                Text(row.risk.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

fun getAlphaColor(score: Double): Color {
    return when {
        score >= 80 -> Color(0xFF2E7D32)
        score >= 60 -> Color(0xFF4CAF50)
        score >= 40 -> Color(0xFFFFC107)
        else -> Color(0xFFD32F2F)
    }
}
