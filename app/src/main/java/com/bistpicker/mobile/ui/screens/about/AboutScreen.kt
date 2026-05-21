package com.bistpicker.mobile.ui.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen() {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    "BIST Picker Hakkında",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Zeki Yatırım Metodolojisi",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            item {
                MethodologyCard(
                    title = "Nasıl Seçim Yapıyoruz?",
                    desc = "BIST Picker, duygulardan arınmış, tamamen matematiksel verilere dayanan çok faktörlü bir seçim algoritması kullanır. Türkiye'nin yüksek enflasyon ve faiz ortamına özel olarak optimize edilmiştir."
                )
            }

            item {
                Text("Kullandığımız Modeller", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ModelDetailItem(
                        icon = Icons.Default.EmojiEvents,
                        title = "Buffett (Kalite)",
                        desc = "Sermaye kârlılığı (ROE) %25'in üzerinde olan, borcu düşük ve rekabet avantajı (Moat) yüksek 'nakit makinelerini' bulur."
                    )
                    ModelDetailItem(
                        icon = Icons.Default.Insights,
                        title = "Graham (Değer)",
                        desc = "Hisseleri varlıklarına ve kârına göre 'kelepir' fiyatlardan almayı hedefler. Reel faize göre iskonto hesabı yapar."
                    )
                    ModelDetailItem(
                        icon = Icons.Default.HealthAndSafety,
                        title = "Piotroski (Güven)",
                        desc = "9 farklı finansal kriterle şirketin operasyonel olarak iyileşip iyileşmediğini denetler. Sessiz krizleri önceden sezer."
                    )
                    ModelDetailItem(
                        icon = Icons.Default.AutoGraph,
                        title = "Momentum (Trend)",
                        desc = "Sadece ucuz olanı değil, piyasanın yükseliş onayını verdiği ve ivme kazanan hisseleri portföye ekler."
                    )
                }
            }

            item {
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Haftalık Disiplin", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Her hafta sonu veriler taranır ve Pazartesi sabahı o haftanın 'En İyi 5'lisi belirlenir. Hafta içi yeni alım önerisi verilmez, stratejiye sadık kalınır.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Text(
                    "Yasal Uyarı: Bu uygulamadaki veriler ve sinyaller yatırım tavsiyesi değildir. Kararlar tamamen kullanıcıya aittir.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun MethodologyCard(title: String, desc: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            Text(desc, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
        }
    }
}

@Composable
fun ModelDetailItem(icon: ImageVector, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
