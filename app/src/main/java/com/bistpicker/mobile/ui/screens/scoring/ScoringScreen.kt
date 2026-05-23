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

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Close
import com.bistpicker.mobile.data.RiskTier
import com.bistpicker.mobile.data.ScoringSortOrder

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
    val sectors by viewModel.sectors.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }

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
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = filters.search ?: "",
                        onValueChange = { viewModel.setSearch(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Hisse ara...", style = MaterialTheme.typography.bodySmall) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        trailingIcon = {
                            if (!filters.search.isNullOrBlank()) {
                                IconButton(onClick = { viewModel.setSearch("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Temizle", modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = { showFilterSheet = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Filtreler")
                    }
                }

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

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, start = 20.dp, end = 20.dp, top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Filtrele ve Sirala", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        TextButton(
                            onClick = { 
                                viewModel.clearFilters()
                                showFilterSheet = false
                            }
                        ) {
                            Text("Sifirla", color = MaterialTheme.colorScheme.error)
                        }
                    }

                    // Sector Selection
                    var sectorExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = sectorExpanded,
                        onExpandedChange = { sectorExpanded = !sectorExpanded }
                    ) {
                        OutlinedTextField(
                            value = filters.sector ?: "Tum Sektorler",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Sektor") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sectorExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                        ExposedDropdownMenu(
                            expanded = sectorExpanded,
                            onDismissRequest = { sectorExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Tum Sektorler") },
                                onClick = {
                                    viewModel.setSector(null)
                                    sectorExpanded = false
                                }
                            )
                            sectors.forEach { sec ->
                                DropdownMenuItem(
                                    text = { Text(sec) },
                                    onClick = {
                                        viewModel.setSector(sec)
                                        sectorExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Risk Tier Selection
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Risk Seviyesi", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(null, RiskTier.LOW, RiskTier.MEDIUM, RiskTier.HIGH).forEach { tier ->
                                val selected = filters.risk == tier
                                FilterChip(
                                    selected = selected,
                                    onClick = { viewModel.setRisk(tier) },
                                    label = { 
                                        Text(
                                            when(tier) {
                                                null -> "Tumu"
                                                RiskTier.LOW -> "Dusuk"
                                                RiskTier.MEDIUM -> "Orta"
                                                RiskTier.HIGH -> "Yuksek"
                                                else -> ""
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // BIST 100 Filter Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sadece BIST 100", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Switch(
                            checked = filters.onlyBist100,
                            onCheckedChange = { viewModel.setOnlyBist100(it) }
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    // Sort Order Choice
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Siralama Olcutu", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            ScoringSortOrder.entries.forEach { order ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.setSortBy(order) }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = filters.sortBy == order,
                                        onClick = { viewModel.setSortBy(order) }
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(order.displayName, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { showFilterSheet = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Uygula")
                    }
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
