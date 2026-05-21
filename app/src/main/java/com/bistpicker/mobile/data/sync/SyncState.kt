package com.bistpicker.mobile.data.sync

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class SyncPhase { Idle, Fetching, Extracting, Applying, Completed, Failed }

data class SyncState(
    val phase: SyncPhase,
    val lastSnapshotDate: String?,
    val lastSyncEpochMs: Long?,
    val lastError: String?,
)

private val Context.syncDataStore by preferencesDataStore(name = "sync_state")

class SnapshotSyncStateStore(private val context: Context) {

    private object Keys {
        val PHASE = stringPreferencesKey("phase")
        val SNAPSHOT_DATE = stringPreferencesKey("snapshot_date")
        val LAST_SYNC = longPreferencesKey("last_sync_epoch_ms")
        val LAST_ERROR = stringPreferencesKey("last_error")
    }

    val state: Flow<SyncState> = context.syncDataStore.data.map { it.toSyncState() }

    suspend fun setPhase(phase: SyncPhase) {
        context.syncDataStore.edit { prefs ->
            prefs[Keys.PHASE] = phase.name
            if (phase != SyncPhase.Failed) prefs.remove(Keys.LAST_ERROR)
        }
    }

    suspend fun markCompleted(snapshotDate: String?) {
        context.syncDataStore.edit { prefs ->
            prefs[Keys.PHASE] = SyncPhase.Completed.name
            prefs[Keys.LAST_SYNC] = System.currentTimeMillis()
            if (snapshotDate != null) prefs[Keys.SNAPSHOT_DATE] = snapshotDate
            prefs.remove(Keys.LAST_ERROR)
        }
    }

    suspend fun markFailed(reason: String) {
        context.syncDataStore.edit { prefs ->
            prefs[Keys.PHASE] = SyncPhase.Failed.name
            prefs[Keys.LAST_ERROR] = reason
        }
    }

    private fun Preferences.toSyncState(): SyncState = SyncState(
        phase = this[Keys.PHASE]?.let { runCatching { SyncPhase.valueOf(it) }.getOrNull() }
            ?: SyncPhase.Idle,
        lastSnapshotDate = this[Keys.SNAPSHOT_DATE],
        lastSyncEpochMs = this[Keys.LAST_SYNC],
        lastError = this[Keys.LAST_ERROR],
    )
}
