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
                    title = "ALPHA Seçim Algoritması Nasıl Çalışır?",
                    desc = "BIST Picker, duygulardan arınmış, tamamen nicel finansal verilere dayanan çok faktörlü bir puanlama algoritması (ALPHA Skoru) kullanır. Bu algoritma, Türkiye'nin yüksek enflasyon ve dalgalı faiz ortamına özel olarak tasarlanmış olup, hisseleri hem global ölçekte hem de kendi sektörüyle kıyaslayarak değerlendirir."
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Model Ağırlıkları", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(12.dp))
                        
                        WeightInfoRow("Buffett Kalite Modeli", "25%")
                        WeightInfoRow("Graham Değer Modeli", "20%")
                        WeightInfoRow("Piotroski Mali Güç", "15%")
                        WeightInfoRow("DCF Güvenlik Marjı (MOS)", "10%")
                        WeightInfoRow("Momentum Gücü & Hacim", "20%")
                        WeightInfoRow("Teknik Trend Takibi", "10%")
                        
                        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        
                        Text(
                            "Not: Hesaplanan ham puanlar, sektör medyanlarına (Sector Benchmarks) göre normalize edilir. Örneğin, bir bankanın kârlılık rasyoları doğrudan sanayi şirketiyle değil, bankacılık sektörü medyanıyla karşılaştırılarak puanlanır.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Text("Finansal Modeller ve Hesaplama Kuralları", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ModelDetailItem(
                        icon = Icons.Default.EmojiEvents,
                        title = "1. Buffett (Kalite Modeli)",
                        desc = "• Özkaynak Kârlılığı (ROE) > %25\n• Aktif Kârlılığı (ROA) > %7\n• Net Borç / VÖKÖK (EBITDA) < 2.0\n• Sürdürülebilir Serbest Nakit Akışı (FCF) üretimi.\n• Amacı: Rekabet avantajı (Moat) yüksek, kâr üreten kaliteli şirketleri seçer."
                    )
                    ModelDetailItem(
                        icon = Icons.Default.Insights,
                        title = "2. Graham (Değer Modeli)",
                        desc = "• Fiyat / Kazanç (F/K) < 15\n• PD / DD < 1.5\n• Graham Çarpanı (F/K * PD/DD) < 22.5\n• Reel Faiz İskontosu: Şirketin kârlılık verimi güncel politika faiziyle karşılaştırılarak iskonto hesaplanır.\n• Amacı: Değerinin altında fiyatlanan 'kelepir' hisseleri yakalar."
                    )
                    ModelDetailItem(
                        icon = Icons.Default.HealthAndSafety,
                        title = "3. Piotroski (Mali Güç - F-Score)",
                        desc = "• 9 Maddelik Check-list:\n  - Kârlılık (Net Kâr > 0, FCF > 0, ROA Artışı vb.) - 4 Puan\n  - Kaldıraç & Likidite (Borç Oranı Azalışı, Cari Oran Artışı vb.) - 3 Puan\n  - Operasyonel Etkinlik (Brüt Marj Artışı, Varlık Devir Artışı) - 2 Puan\n• Puanlama: 7-9 Puan = Güçlü, 4-6 Puan = Nötr, 0-3 Puan = Riskli."
                    )
                    ModelDetailItem(
                        icon = Icons.Default.AutoGraph,
                        title = "4. DCF & Güvenlik Marjı (MOS)",
                        desc = "• İndirgenmiş Nakit Akışı (DCF) Değerlemesi:\n  - 10 yıllık Serbest Nakit Akış projeksiyonu.\n  - Türkiye ülke risk primine göre ayarlanmış Ağırlıklı Ortalama Sermaye Maliyeti (WACC) iskonto oranı.\n  - İçsel Değer (Intrinsic Value) hesaplanır.\n  - Güvenlik Marjı (MOS) = (İçsel Değer / Güncel Fiyat - 1) * 100\n• Hedef: En az %20-30 güvenlik marjına sahip hisselere odaklanır."
                    )
                    ModelDetailItem(
                        icon = Icons.Default.AutoGraph,
                        title = "5. Momentum & Teknik Güç",
                        desc = "• Göreceli Momentum: Hisse fiyatının 3, 6 ve 12 aylık periyotlarda endeks üzerindeki rölatif performansı.\n• Teknik İvme: Fiyatın 200 günlük hareketli ortalamanın (MA200) üzerinde olması ve işlem hacmindeki artış ivmesi."
                    )
                }
            }

            item {
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Haftalık Portföy Disiplini", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Veritabanı her hafta sonu cuma kapanış verileriyle güncellenir. Pazartesi sabahı ALPHA skoru en yüksek olan 5 hisse model portföye dahil edilir. Hafta içinde manuel alım-satım yapılmaz ve disipline sadık kalınır.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Text(
                    "Yasal Uyarı: BIST Picker tarafından sunulan puanlar, analizler ve sinyaller tamamen matematiksel algoritmalara dayanmaktadır ve yatırım tavsiyesi niteliği taşımaz. Yatırım kararlarının sorumluluğu tamamen kullanıcıya aittir.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun WeightInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun MethodologyCard(title: String, desc: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(desc, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
        }
    }
}

@Composable
fun ModelDetailItem(icon: ImageVector, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp).padding(top = 2.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(2.dp))
            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
        }
    }
}
