package com.bistpicker.mobile.data.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    @Volatile
    private var cachedCookies: String? = null
    @Volatile
    private var cachedCrumb: String? = null

    private val lock = Any()

    private fun getCookieAndCrumb(): Pair<String, String>? {
        synchronized(lock) {
            val cc = cachedCookies
            val cr = cachedCrumb
            if (cc != null && cr != null) {
                return Pair(cc, cr)
            }

            try {
                Log.d("LivePriceClient", "Performing cookie & crumb handshake...")
                val req1 = Request.Builder()
                    .url("https://fc.yahoo.com")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .build()
                val resp1 = httpClient.newCall(req1).execute()
                val cookieHeaders = resp1.headers("Set-Cookie")
                if (cookieHeaders.isEmpty()) {
                    Log.w("LivePriceClient", "No Set-Cookie headers received from fc.yahoo.com")
                    return null
                }
                val cookies = cookieHeaders.map { it.split(";").first() }.joinToString("; ")
                Log.d("LivePriceClient", "Extracted cookies: $cookies")

                val req2 = Request.Builder()
                    .url("https://query1.finance.yahoo.com/v1/test/getcrumb")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Cookie", cookies)
                    .build()
                val resp2 = httpClient.newCall(req2).execute()
                if (!resp2.isSuccessful) {
                    Log.w("LivePriceClient", "Failed to fetch crumb: ${resp2.code}")
                    return null
                }
                val crumb = resp2.body?.string()?.trim()
                if (crumb.isNullOrEmpty() || crumb.contains("<html>")) {
                    Log.w("LivePriceClient", "Invalid crumb received: $crumb")
                    return null
                }
                Log.d("LivePriceClient", "Successfully fetched crumb: $crumb")
                cachedCookies = cookies
                cachedCrumb = crumb
                return Pair(cookies, crumb)
            } catch (e: Exception) {
                Log.e("LivePriceClient", "Exception during handshake", e)
                return null
            }
        }
    }

    private fun clearCache() {
        synchronized(lock) {
            cachedCookies = null
            cachedCrumb = null
        }
    }

    suspend fun fetchPrices(tickers: List<String>): Map<String, Double> = withContext(Dispatchers.IO) {
        if (tickers.isEmpty()) return@withContext emptyMap()

        var handshake = getCookieAndCrumb()
        if (handshake == null) {
            Log.w("LivePriceClient", "Cannot fetch prices: handshake failed")
            return@withContext emptyMap()
        }

        var cookies = handshake.first
        var crumb = handshake.second
        val symbols = tickers.joinToString(",") { "$it.IS" }
        var url = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=$symbols&crumb=$crumb"
        Log.d("LivePriceClient", "Fetching live prices from URL: $url")

        var request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .header("Cookie", cookies)
            .build()

        try {
            var response = httpClient.newCall(request).execute()
            if (response.code == 401) {
                Log.w("LivePriceClient", "Received 401. Retrying with fresh handshake...")
                clearCache()
                handshake = getCookieAndCrumb() ?: return@withContext emptyMap()
                cookies = handshake.first
                crumb = handshake.second
                url = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=$symbols&crumb=$crumb"
                request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Cookie", cookies)
                    .build()
                response = httpClient.newCall(request).execute()
            }

            Log.d("LivePriceClient", "Response code: ${response.code}")
            if (!response.isSuccessful) {
                Log.w("LivePriceClient", "Response not successful: ${response.message}")
                return@withContext emptyMap()
            }

            val body = response.body?.string() ?: run {
                Log.w("LivePriceClient", "Empty response body")
                return@withContext emptyMap()
            }
            val jsonRoot = json.parseToJsonElement(body).jsonObject
            val resultList = jsonRoot["quoteResponse"]?.jsonObject?.get("result")?.jsonArray ?: run {
                Log.w("LivePriceClient", "Could not parse result array")
                return@withContext emptyMap()
            }

            val priceMap = mutableMapOf<String, Double>()
            resultList.forEach { element ->
                val obj = element.jsonObject
                val rawSymbol = obj["symbol"]?.jsonPrimitive?.content ?: ""
                val symbol = if (rawSymbol.endsWith(".IS")) rawSymbol.removeSuffix(".IS") else rawSymbol
                val price = obj["regularMarketPrice"]?.jsonPrimitive?.content?.toDoubleOrNull()
                if (symbol.isNotEmpty() && price != null) {
                    priceMap[symbol] = price
                }
            }
            Log.d("LivePriceClient", "Successfully parsed prices: $priceMap")
            priceMap
        } catch (e: Exception) {
            Log.e("LivePriceClient", "Error fetching prices from Yahoo Finance", e)
            emptyMap()
        }
    }
}
