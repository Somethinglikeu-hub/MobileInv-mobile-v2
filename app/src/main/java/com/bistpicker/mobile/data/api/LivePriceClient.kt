package com.bistpicker.mobile.data.api

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Lightweight client to fetch live prices from Yahoo Finance.
 * Practical approach: Only for active portfolio and top picks.
 */
class LivePriceClient(
    private val httpClient: OkHttpClient,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    suspend fun fetchPrices(tickers: List<String>): Map<String, Double> {
        if (tickers.isEmpty()) return emptyMap()

        // Yahoo Finance uses .IS suffix for BIST stocks (e.g. THYAO.IS)
        val symbols = tickers.joinToString(",") { "$it.IS" }
        val url = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=$symbols"

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0")
            .build()

        return try {
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return emptyMap()

            val body = response.body?.string() ?: return emptyMap()
            val jsonRoot = json.parseToJsonElement(body).jsonObject
            val resultList = jsonRoot["quoteResponse"]?.jsonObject?.get("result")?.jsonArray ?: return emptyMap()

            val priceMap = mutableMapOf<String, Double>()
            resultList.forEach { element ->
                val obj = element.jsonObject
                val symbol = obj["symbol"]?.jsonPrimitive?.content?.removeSuffix(".IS") ?: ""
                val price = obj["regularMarketPrice"]?.jsonPrimitive?.content?.toDoubleOrNull()
                if (symbol.isNotEmpty() && price != null) {
                    priceMap[symbol] = price
                }
            }
            priceMap
        } catch (e: Exception) {
            emptyMap()
        }
    }
}
