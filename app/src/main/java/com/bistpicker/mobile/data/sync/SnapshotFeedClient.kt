package com.bistpicker.mobile.data.sync

import com.bistpicker.mobile.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.util.zip.GZIPInputStream

/**
 * Fetch the manifest, validate it, download the snapshot blob, verify
 * sha256, and decompress to a target file. Pure I/O — applying the
 * decompressed DB into Room belongs to [SnapshotApplier].
 */
class SnapshotFeedClient(
    private val httpClient: OkHttpClient,
    private val manifestUrl: String,
    private val json: Json = DefaultJson,
) {

    suspend fun fetchManifest(): SnapshotManifest {
        val request = Request.Builder()
            .url(manifestUrl)
            .header("User-Agent", USER_AGENT)
            .header("Accept", "application/json")
            .build()
        httpClient.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) {
                throw IOException("Manifest fetch failed: HTTP ${resp.code}")
            }
            val body = resp.body?.string() ?: throw IOException("Empty manifest body")
            return json.decodeFromString(SnapshotManifest.serializer(), body)
        }
    }

    /**
     * Download + verify + decompress into [target]. Caller must have
     * already validated the schema version against
     * [SCHEMA_VERSION_MIN_SUPPORTED]. Returns the size of the
     * decompressed file in bytes.
     */
    suspend fun downloadAndExtract(
        payload: SnapshotPayload,
        target: File,
    ): Long {
        require(payload.compression.equals("gzip", ignoreCase = true)) {
            "Unsupported compression: ${payload.compression}"
        }
        val downloadUrl = if (BuildConfig.DEBUG && payload.url.contains("raw.githubusercontent.com")) {
            payload.url.replace("https://raw.githubusercontent.com/Somethinglikeu-hub/MobileInv-feed/gh-pages/", "http://10.0.2.2:8000/")
        } else {
            payload.url
        }
        val request = Request.Builder()
            .url(downloadUrl)
            .header("User-Agent", USER_AGENT)
            .build()
        httpClient.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) {
                throw IOException("Snapshot fetch failed: HTTP ${resp.code}")
            }
            val body = resp.body ?: throw IOException("Empty snapshot body")
            val bytes = body.bytes()
            if (bytes.size.toLong() != payload.sizeBytes) {
                throw IOException(
                    "Snapshot size mismatch: expected=${payload.sizeBytes} actual=${bytes.size}"
                )
            }
            val sha = sha256Hex(bytes)
            if (!sha.equals(payload.sha256, ignoreCase = true)) {
                throw IOException("Snapshot sha256 mismatch")
            }
            target.parentFile?.mkdirs()
            // Decompress to a temp sibling so a partial write never
            // corrupts an existing snapshot file.
            val tmp = File(target.parentFile, "${target.name}.tmp")
            try {
                tmp.outputStream().use { out ->
                    GZIPInputStream(bytes.inputStream()).copyTo(out)
                }
                if (!tmp.renameTo(target)) {
                    target.delete()
                    if (!tmp.renameTo(target)) {
                        throw IOException("Failed to publish snapshot to ${target.absolutePath}")
                    }
                }
                return target.length()
            } finally {
                if (tmp.exists()) tmp.delete()
            }
        }
    }

    private fun sha256Hex(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }

    companion object {
        const val SCHEMA_VERSION_MIN_SUPPORTED: Int = 1
        const val USER_AGENT: String = "BistPickerMobile/2.0 (Android)"
        val DefaultJson: Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}
