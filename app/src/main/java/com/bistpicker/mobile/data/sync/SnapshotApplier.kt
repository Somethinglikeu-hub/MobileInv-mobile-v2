package com.bistpicker.mobile.data.sync

import android.content.Context
import com.bistpicker.mobile.data.local.SnapshotDatabase
import java.io.File
import java.io.IOException

/**
 * Validates a candidate snapshot DB on disk and atomically promotes it to
 * the canonical location consumed by Room.
 *
 * Validation steps:
 *   1. File exists, non-zero size, opens as SQLite.
 *   2. snapshot_metadata.id=1 row is present.
 *   3. schema_version >= SCHEMA_VERSION_MIN_SUPPORTED (BuildConfig).
 *   4. Required tables (smoke list) are present.
 *
 * On success: closes the active Room handle, replaces the canonical file,
 * and lets [AppContainer] rebuild Room against the new file. On failure:
 * deletes the candidate, leaves the existing canonical file untouched, and
 * surfaces the reason via [SnapshotImportResult].
 */
class SnapshotApplier(
    private val context: Context,
) {

    fun seedFromBundleIfMissing(): Boolean {
        val canonical = SnapshotDatabase.canonicalDbFile(context)
        if (canonical.exists() && canonical.length() > 0) return false

        canonical.parentFile?.mkdirs()
        return runCatching {
            context.assets.open(BUNDLED_ASSET_FILENAME).use { input ->
                canonical.outputStream().use { output -> input.copyTo(output) }
            }
        }.isSuccess
    }

    fun apply(candidate: File): SnapshotImportResult {
        if (!candidate.exists() || candidate.length() == 0L) {
            return SnapshotImportResult.Failed("Candidate snapshot empty or missing")
        }
        // Validate via a temporary Room instance so we don't disturb the
        // active one. We open by file path with a throwaway DB name.
        return try {
            validate(candidate)
            promote(candidate)
            SnapshotImportResult.Success
        } catch (e: Exception) {
            candidate.delete()
            SnapshotImportResult.Failed(e.message ?: "validation failed")
        }
    }

    private fun validate(candidate: File) {
        // Lightweight raw SQLite check — saves spinning up a Room instance
        // and means we don't depend on Room's schema-mismatch behavior.
        val db = android.database.sqlite.SQLiteDatabase.openDatabase(
            candidate.absolutePath,
            null,
            android.database.sqlite.SQLiteDatabase.OPEN_READONLY,
        )
        try {
            val version = db.rawQuery(
                "SELECT schema_version FROM snapshot_metadata WHERE id = 1",
                null,
            ).use { c ->
                if (!c.moveToFirst()) error("snapshot_metadata row missing")
                c.getInt(0)
            }
            if (version < SnapshotFeedClient.SCHEMA_VERSION_MIN_SUPPORTED) {
                error("schema_version $version below minimum ${SnapshotFeedClient.SCHEMA_VERSION_MIN_SUPPORTED}")
            }
            REQUIRED_TABLES.forEach { table ->
                db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                    arrayOf(table),
                ).use { c ->
                    if (!c.moveToFirst()) error("missing required table: $table")
                }
            }
        } finally {
            db.close()
        }
    }

    private fun promote(candidate: File) {
        val canonical = SnapshotDatabase.canonicalDbFile(context)
        canonical.parentFile?.mkdirs()
        // Best-effort cleanup of WAL/SHM siblings that Room may have left.
        listOf("-wal", "-shm").forEach { suffix ->
            File(canonical.absolutePath + suffix).delete()
        }
        if (!candidate.renameTo(canonical)) {
            // Fallback to copy+delete when renameTo fails (e.g. across FS).
            candidate.inputStream().use { input ->
                canonical.outputStream().use { output -> input.copyTo(output) }
            }
            candidate.delete()
        }
        if (!canonical.exists() || canonical.length() == 0L) {
            throw IOException("Promotion failed — canonical file empty")
        }
    }

    companion object {
        const val BUNDLED_ASSET_FILENAME = "mobile_snapshot.db"

        // Smoke list of tables the APK actually needs. Adding a column is
        // backward compatible; adding a table requires a snapshot_version
        // bump and an entry here.
        private val REQUIRED_TABLES = listOf(
            "snapshot_metadata",
            "home_summary",
            "open_positions",
            "scoring_latest",
            "companies",
        )
    }
}

sealed interface SnapshotImportResult {
    data object Success : SnapshotImportResult
    data class Failed(val reason: String) : SnapshotImportResult
}
