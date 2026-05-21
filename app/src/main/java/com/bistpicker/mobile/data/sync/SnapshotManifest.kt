package com.bistpicker.mobile.data.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wire format for the manifest published at the configured feed URL.
 * Mirrors the JSON written by `bist_picker.mobile_feed.export_mobile_feed`:
 *
 *     {
 *       "snapshot_version": 2,
 *       "exported_at": "2026-05-08T16:18:42+00:00",
 *       "snapshot": {
 *         "filename": "mobile_snapshot.db.gz",
 *         "url": "https://.../mobile_snapshot.db.gz",
 *         "sha256": "...",
 *         "size_bytes": 12345678,
 *         "compression": "gzip"
 *       }
 *     }
 *
 * Anything we don't recognise is ignored — we only fail the sync if a
 * required field is missing or the schema version is below
 * [SCHEMA_VERSION_MIN_SUPPORTED].
 */
@Serializable
data class SnapshotManifest(
    @SerialName("snapshot_version") val snapshotVersion: Int,
    @SerialName("exported_at") val exportedAt: String,
    val snapshot: SnapshotPayload,
)

@Serializable
data class SnapshotPayload(
    val filename: String,
    val url: String,
    val sha256: String,
    @SerialName("size_bytes") val sizeBytes: Long,
    val compression: String = "gzip",
)
