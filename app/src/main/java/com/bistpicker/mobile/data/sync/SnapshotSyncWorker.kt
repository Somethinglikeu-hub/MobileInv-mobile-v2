package com.bistpicker.mobile.data.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bistpicker.mobile.AppContainer
import com.bistpicker.mobile.AppContainerProvider
import com.bistpicker.mobile.data.local.SnapshotDatabase
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Periodic snapshot refresh. Schedules itself every 6h with a metered-data
 * cap so we don't burn the user's mobile data when there's nothing new.
 *
 * Runs the full sync pipeline:
 *   1. Fetch manifest JSON.
 *   2. Validate snapshot_version >= MIN.
 *   3. Download + verify + decompress to a candidate file.
 *   4. Hand off to [SnapshotApplier] which atomically promotes it.
 *   5. Rebuild Room handle so observers see the fresh data.
 */
class SnapshotSyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val container: AppContainer = AppContainerProvider.get(applicationContext)
        val state = container.syncState
        val client = container.feedClient
        val applier = container.applier

        return try {
            state.setPhase(SyncPhase.Fetching)
            val manifest = client.fetchManifest()
            if (manifest.snapshotVersion < SnapshotFeedClient.SCHEMA_VERSION_MIN_SUPPORTED) {
                throw IllegalStateException(
                    "Snapshot version ${manifest.snapshotVersion} below minimum " +
                        SnapshotFeedClient.SCHEMA_VERSION_MIN_SUPPORTED
                )
            }

            state.setPhase(SyncPhase.Extracting)
            val candidate = File(applicationContext.cacheDir, "snapshot_candidate.db")
            client.downloadAndExtract(manifest.snapshot, candidate)

            state.setPhase(SyncPhase.Applying)
            when (val outcome = applier.apply(candidate)) {
                SnapshotImportResult.Success -> {
                    container.rebuildDatabase()
                    state.markCompleted(snapshotDate = container.metadataSnapshotDate())
                    Result.success()
                }
                is SnapshotImportResult.Failed -> {
                    state.markFailed(outcome.reason)
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            state.markFailed(e.message ?: e::class.java.simpleName)
            Result.retry()
        }
    }

    companion object {
        const val UNIQUE_PERIODIC_NAME = "bist_snapshot_periodic"

        fun enqueuePeriodic(context: Context) {
            val request = PeriodicWorkRequestBuilder<SnapshotSyncWorker>(
                repeatInterval = 6,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                flexTimeInterval = 1,
                flexTimeIntervalUnit = TimeUnit.HOURS,
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request,
            )
        }
    }
}
